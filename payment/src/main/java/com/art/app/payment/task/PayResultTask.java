package com.art.app.payment.task;

import com.art.app.common.Constants;
import com.art.app.common.bean.FactoryList;
import com.art.app.common.enums.PaymentStatusEnum;
import com.art.app.common.pattern.IPaymentStatusSubject;
import com.art.app.common.pattern.entity.PaymentStatusParams;
import com.art.app.common.task.AbstractTask;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.mw.redis.RedisUtil;
import com.art.app.orm.entity.PaymentOrderInfo;
import com.art.app.orm.service.PaymentOrderInfoService;
import com.art.app.payment.PaymentException;
import com.art.app.payment.entity.QueryPayResultRequest;
import com.art.app.payment.entity.QueryPayResultResponse;
import com.art.app.payment.enums.PaymentErrorCodeEnum;
import com.art.app.payment.service.IPaymentTypeService;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class PayResultTask extends AbstractTask {

    private static final Logger log = LoggerFactory.getLogger("tasklogger");

    @Resource
    private PaymentOrderInfoService paymentOrderInfoService;
    @Resource
    private FactoryList<Integer, IPaymentTypeService> iPaymentTypeServices;
    @Resource
    private IPaymentStatusSubject iPaymentStatusSubject;
    @Resource
    private RedisUtil redisUtil;

    @Override
    protected void dealJob() {
        int tmpNumbers = 0;
        Integer id = 0;
        Wrapper numberWrap = Condition.create()
                .eq("del_flag", 0)
                .and().eq("payment_status", PaymentStatusEnum.PAYING.getStatus());
        Integer numbers = paymentOrderInfoService.selectCount(numberWrap);
        log.info("查询支付结果，需要处理的数量 -> {}", numbers);
        Date nowDate = DatetimeUtils.now();
        while (null != numbers && tmpNumbers < numbers) {
            try {
                Wrapper listWrap = Condition.create()
                        .eq("del_flag", 0)
                        .and().eq("payment_status", PaymentStatusEnum.PAYING.getStatus())
                        .and().gt("id", id)
                        .last("limit " + PER_QUERY_NUMBERS);
                List<PaymentOrderInfo> paymentOrderInfos = paymentOrderInfoService.selectList(listWrap);
                if (!CollectionUtils.isEmpty(paymentOrderInfos)) {
                    for (PaymentOrderInfo paymentOrderInfo : paymentOrderInfos) {
                        if (PaymentStatusEnum.PAYING.getStatus().equals(paymentOrderInfo.getPaymentStatus())) {
                            String key = Constants.ORDER_FEEDBACK_PREFIX + paymentOrderInfo.getPaymentOrderId();
                            String value = UUID.randomUUID().toString();
                            boolean isLocked = false;
                            try {
                                isLocked = redisUtil.lock(key, value, 2, TimeUnit.SECONDS);
                                if (isLocked) {
                                    dealPaymentOrderInfo(paymentOrderInfo, nowDate);
                                } else {
                                    log.info("查询支付结果加锁失败");
                                }
                            } catch (Exception e) {
                                log.error("查询支付结果异常 -> " + paymentOrderInfo.getPaymentOrderId(), e);
                            } finally {
                                id = id < paymentOrderInfo.getId() ? paymentOrderInfo.getId() : id;
                                if (isLocked) {
                                    boolean isUnLock = redisUtil.unLock(key, value);
                                    if (!isUnLock) {
                                        log.info("查询支付结果解锁失败");
                                    }
                                }
                            }
                        } else {
                            // 状态已变更，不需要再处理
                            id = id < paymentOrderInfo.getId() ? paymentOrderInfo.getId() : id;
                        }
                    }
                } else {
                    break;
                }
            } catch (Exception e) {
                log.error("查询支付结果异常", e);
            } finally {
                tmpNumbers += PER_QUERY_NUMBERS;
            }
        }
    }

    private void dealPaymentOrderInfo(PaymentOrderInfo paymentOrderInfo, Date nowDate) {
        QueryPayResultRequest queryPayResultRequest = new QueryPayResultRequest();
        queryPayResultRequest.setOrderId(paymentOrderInfo.getArtOrderId());
        QueryPayResultResponse payResultResponse = null;
        try {
            payResultResponse = iPaymentTypeServices.getBean(paymentOrderInfo.getPaymentType())
                    .queryPaymentTypePayResult(paymentOrderInfo);
        } catch (Exception e) {
            if (e instanceof PaymentException
                    && PaymentErrorCodeEnum.OUT_TRADE_NO_NOT_EXIST.getCode() == ((PaymentException) e).getCode()
                    && DatetimeUtils.addMinute(nowDate, -Constants.MAX_PAYMENT_TIME).after(paymentOrderInfo.getCreatedAt())) {
                // 如果超过订单最大支付时间返回订单不存在，更新订单状态为
                PaymentOrderInfo updateInfo = new PaymentOrderInfo();
                updateInfo.setId(paymentOrderInfo.getId());
                updateInfo.setPaymentStatus(PaymentStatusEnum.PAY_FAILED.getStatus());
                updateInfo.setPaymentStatusRemark(e.getMessage());
                updateInfo.setUpdatedAt(DatetimeUtils.now());
                paymentOrderInfoService.updateById(updateInfo);
                paymentStatusChanged(paymentOrderInfo, updateInfo.getPaymentStatus());
            }
            throw e;
        }
        if (PaymentStatusEnum.isFinalStatus(payResultResponse.getResult())) {
            PaymentOrderInfo updateInfo = new PaymentOrderInfo();
            updateInfo.setId(paymentOrderInfo.getId());
            updateInfo.setOutOrderId(payResultResponse.getOutOrderId());
            updateInfo.setPaymentStatus(payResultResponse.getResult());
            updateInfo.setPaymentStatusRemark(payResultResponse.getRemark());
            updateInfo.setUpdatedAt(DatetimeUtils.now());
            paymentOrderInfoService.updateById(updateInfo);
            paymentStatusChanged(paymentOrderInfo, updateInfo.getPaymentStatus());
        } else {
            if (nowDate.after(paymentOrderInfo.getTimeExpire())) {
                log.info("订单[{}]支付时间过期, 状态 -> {}, 当前时间 -> {}, 支付截止时间 -> {}",
                        paymentOrderInfo.getPaymentOrderId(), payResultResponse.getResult(),
                        DatetimeUtils.formatDatetime(nowDate), DatetimeUtils.formatDatetime(paymentOrderInfo.getTimeExpire()));
                PaymentOrderInfo updateInfo = new PaymentOrderInfo();
                updateInfo.setId(paymentOrderInfo.getId());
                updateInfo.setPaymentStatus(PaymentStatusEnum.CANCELED.getStatus());
                updateInfo.setPaymentStatusRemark("支付超时");
                updateInfo.setUpdatedAt(DatetimeUtils.now());
                paymentOrderInfoService.updateById(updateInfo);
                paymentStatusChanged(paymentOrderInfo, updateInfo.getPaymentStatus());
            }
        }
    }

    private void paymentStatusChanged(PaymentOrderInfo paymentOrderInfo, Integer paymentStatus) {
        PaymentStatusParams params = new PaymentStatusParams();
        params.setArtOrderId(paymentOrderInfo.getArtOrderId());
        params.setArtOrderType(paymentOrderInfo.getArtOrderType());
        params.setPaymentOrderId(paymentOrderInfo.getPaymentOrderId());
        params.setPaymentStatus(paymentStatus);
        params.setPaymentType(paymentOrderInfo.getPaymentType());
        iPaymentStatusSubject.accept(params);
    }

}
