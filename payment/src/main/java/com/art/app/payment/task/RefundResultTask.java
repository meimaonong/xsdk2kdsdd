package com.art.app.payment.task;

import com.art.app.common.Constants;
import com.art.app.common.bean.FactoryList;
import com.art.app.common.pattern.IRefundStatusSubject;
import com.art.app.common.pattern.entity.RefundStatusParams;
import com.art.app.common.task.AbstractTask;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.mw.redis.RedisUtil;
import com.art.app.orm.entity.PaymentRefundOrderInfo;
import com.art.app.orm.service.PaymentRefundOrderInfoService;
import com.art.app.payment.PaymentException;
import com.art.app.payment.entity.QueryPayResultRequest;
import com.art.app.payment.entity.QueryRefundResultResponse;
import com.art.app.payment.enums.PaymentErrorCodeEnum;
import com.art.app.payment.enums.RefundStatusEnum;
import com.art.app.payment.service.IPaymentTypeService;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RefundResultTask extends AbstractTask {

    private static final Logger log = LoggerFactory.getLogger("tasklogger");

    @Resource
    private PaymentRefundOrderInfoService paymentRefundOrderInfoService;
    @Resource
    private FactoryList<Integer, IPaymentTypeService> iPaymentTypeServices;
    @Resource
    private IRefundStatusSubject iRefundStatusSubject;
    @Resource
    private RedisUtil redisUtil;

    @Override
    protected void dealJob() {
        int tmpNumbers = 0;
        Integer id = 0;
        Wrapper numberWrap = Condition.create()
                .eq("del_flag", 0)
                .and().eq("refund_status", RefundStatusEnum.REFUNDING.getRefundStatus());
        Integer numbers = paymentRefundOrderInfoService.selectCount(numberWrap);
        log.info("需要处理的数量 -> {}", numbers);
        while (null != numbers && tmpNumbers < numbers) {
            try {
                Wrapper listWrap = Condition.create()
                        .eq("del_flag", 0)
                        .and().eq("refund_status", RefundStatusEnum.REFUNDING.getRefundStatus())
                        .and().gt("id", id)
                        .last("limit " + PER_QUERY_NUMBERS);
                List<PaymentRefundOrderInfo> paymentRefundOrderInfos = paymentRefundOrderInfoService.selectList(listWrap);
                if (!CollectionUtils.isEmpty(paymentRefundOrderInfos)) {
                    for (PaymentRefundOrderInfo refundOrderInfo : paymentRefundOrderInfos) {
                        if (RefundStatusEnum.REFUNDING.getRefundStatus().equals(refundOrderInfo.getRefundStatus())) {
                            String key = Constants.REFUND_FEEDBACK_PREFIX + refundOrderInfo.getRefundOrderId();
                            String value = UUID.randomUUID().toString();
                            boolean isLocked = false;
                            try {
                                isLocked = redisUtil.lock(key, value, 2, TimeUnit.SECONDS);
                                if (isLocked) {
                                    dealRefundOrderInfo(refundOrderInfo);
                                } else {
                                    log.info("查询退款结果加锁失败");
                                }
                            } catch (Exception e) {
                                log.error("查询退款结果异常 -> " + refundOrderInfo.getPaymentOrderId(), e);
                            } finally {
                                id = id < refundOrderInfo.getId() ? refundOrderInfo.getId() : id;
                                if (isLocked) {
                                    boolean isUnLock = redisUtil.unLock(key, value);
                                    if (!isUnLock) {
                                        log.info("查询支付结果解锁失败");
                                    }
                                }
                            }
                        } else {
                            // 状态已变更，不需要再处理
                            id = id < refundOrderInfo.getId() ? refundOrderInfo.getId() : id;
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

    private void dealRefundOrderInfo(PaymentRefundOrderInfo refundOrderInfo) {
        QueryPayResultRequest queryPayResultRequest = new QueryPayResultRequest();
        queryPayResultRequest.setOrderId(refundOrderInfo.getArtOrderId());
        QueryRefundResultResponse refundResultResponse = null;
        try {
            refundResultResponse = iPaymentTypeServices.getBean(refundOrderInfo.getPaymentType())
                    .queryRefundResult(refundOrderInfo);
        } catch (Exception e) {
            if (e instanceof PaymentException
                    && PaymentErrorCodeEnum.OUT_TRADE_NO_NOT_EXIST.getCode() == ((PaymentException) e).getCode()
                    && DatetimeUtils.addMinute(DatetimeUtils.now(), -10).after(refundOrderInfo.getCreatedAt())) {
                // 如果返回订单不存在，更新订单状态为
                PaymentRefundOrderInfo updateInfo = new PaymentRefundOrderInfo();
                updateInfo.setId(refundOrderInfo.getId());
                updateInfo.setRefundStatus(RefundStatusEnum.REFUND_FAILED.getRefundStatus());
                updateInfo.setRefundStatusRemark(e.getMessage());
                updateInfo.setUpdatedAt(DatetimeUtils.now());
                paymentRefundOrderInfoService.updateById(updateInfo);
                refundStatusChanged(refundOrderInfo, updateInfo.getRefundStatus());
            }
            throw e;
        }
        if (RefundStatusEnum.isFinalStatus(refundResultResponse.getResult())) {
            PaymentRefundOrderInfo updateInfo = new PaymentRefundOrderInfo();
            updateInfo.setId(refundOrderInfo.getId());
            updateInfo.setRefundStatus(refundResultResponse.getResult());
            updateInfo.setRefundStatusRemark(refundResultResponse.getRemark());
            updateInfo.setUpdatedAt(DatetimeUtils.now());
            paymentRefundOrderInfoService.updateById(updateInfo);
            refundStatusChanged(refundOrderInfo, updateInfo.getRefundStatus());
        }
    }

    private void refundStatusChanged(PaymentRefundOrderInfo refundOrderInfo, Integer refundStatus) {
        RefundStatusParams params = new RefundStatusParams();
        params.setArtOrderId(refundOrderInfo.getArtOrderId());
        params.setArtOrderType(refundOrderInfo.getArtOrderType());
        params.setPaymentOrderId(refundOrderInfo.getPaymentOrderId());
        params.setRefundOrderId(refundOrderInfo.getRefundOrderId());
        params.setRefundStatus(refundStatus);
        iRefundStatusSubject.accept(params);
    }

}
