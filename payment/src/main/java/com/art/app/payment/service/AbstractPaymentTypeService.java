package com.art.app.payment.service;

import com.art.app.common.Constants;
import com.art.app.common.enums.PaymentStatusEnum;
import com.art.app.common.pattern.IPaymentStatusSubject;
import com.art.app.common.pattern.IRefundStatusSubject;
import com.art.app.common.pattern.entity.PaymentStatusParams;
import com.art.app.common.pattern.entity.RefundStatusParams;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.common.util.OrderUtils;
import com.art.app.mw.redis.RedisUtil;
import com.art.app.orm.entity.PaymentOrderInfo;
import com.art.app.orm.entity.PaymentOrderLogInfo;
import com.art.app.orm.entity.PaymentRefundOrderInfo;
import com.art.app.orm.service.PaymentOrderInfoService;
import com.art.app.orm.service.PaymentOrderLogInfoService;
import com.art.app.orm.service.PaymentRefundOrderInfoService;
import com.art.app.payment.entity.FeedbackParam;
import com.art.app.payment.entity.PaymentOrderExtendInfo;
import com.art.app.payment.entity.PaymentTypeRefundParam;
import com.art.app.payment.entity.PrePayRequest;
import com.art.app.payment.entity.PrePayResponse;
import com.art.app.payment.entity.PrePayResponseParams;
import com.art.app.payment.entity.QueryRefundResultResponse;
import com.art.app.payment.entity.RefundRequest;
import com.art.app.payment.enums.PaymentLogTypeEnum;
import com.art.app.payment.enums.RefundStatusEnum;
import com.art.app.payment.utils.JackJsonUtil;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractPaymentTypeService implements IPaymentTypeService {

    @Resource
    private PaymentOrderInfoService paymentOrderInfoService;
    @Resource
    private PaymentRefundOrderInfoService paymentRefundOrderInfoService;
    @Resource
    private PaymentOrderLogInfoService paymentOrderLogInfoService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IPaymentStatusSubject iPaymentStatusSubject;
    @Resource
    private IRefundStatusSubject iRefundStatusSubject;

    @Override
    public PrePayResponse prePay(PrePayRequest prePayRequest) {
        // 生成支付订单
        PaymentOrderInfo paymentOrderInfo = new PaymentOrderInfo();
        paymentOrderInfo.setArtOrderId(prePayRequest.getOrderId());
        paymentOrderInfo.setArtOrderType(prePayRequest.getOrderType());
        paymentOrderInfo.setPaymentOrderId(OrderUtils.createOrderId("PAY"));
        paymentOrderInfo.setPaymentType(prePayRequest.getPaymentType());
        paymentOrderInfo.setPaymentStatus(PaymentStatusEnum.PAYING.getStatus());
        paymentOrderInfo.setTotalAmount(prePayRequest.getAmount());
        paymentOrderInfo.setSubject(prePayRequest.getSubject());
        paymentOrderInfo.setTimeExpire(prePayRequest.getTimeExpire());
        paymentOrderInfo.setNotifyUrl(createPrePayNotifyUrl());
        paymentOrderInfo.setRemark("");
        paymentOrderInfo.setCreatedAt(DatetimeUtils.now());
        paymentOrderInfo.setUpdatedAt(paymentOrderInfo.getCreatedAt());
        paymentOrderInfo.setDelFlag(0);
        paymentOrderInfoService.insert(paymentOrderInfo);
        // 先保存数据再调用对方支付接口，防止调用接口以后保存数据失败，导致数据丢失
        PrePayResponseParams param = null;
        try {
            param = paymentTypePrePay(prePayRequest, paymentOrderInfo);
        } catch (Exception e) {
            // 更新失败状态
            PaymentOrderInfo updateInfo = new PaymentOrderInfo();
            updateInfo.setId(paymentOrderInfo.getId());
            updateInfo.setPaymentStatus(PaymentStatusEnum.PAY_FAILED.getStatus());
            updateInfo.setUpdatedAt(DatetimeUtils.now());
            paymentOrderInfoService.updateById(updateInfo);
            throw e;
        }
        PrePayResponse<PrePayResponseParams> prePayResponse = new PrePayResponse<>();
        prePayResponse.setPaymentType(prePayRequest.getPaymentType());
        prePayResponse.setParams(param);
        return prePayResponse;
    }

    @Override
    public void refund(RefundRequest refundRequest, PaymentOrderInfo paymentOrderInfo) {
        // 生成退款订单
        PaymentRefundOrderInfo refundOrderInfo = new PaymentRefundOrderInfo();
        refundOrderInfo.setArtOrderId(refundRequest.getOrderId());
        refundOrderInfo.setPaymentOrderId(paymentOrderInfo.getPaymentOrderId());
        refundOrderInfo.setRefundOrderId(OrderUtils.createOrderId("REFUND"));
        refundOrderInfo.setPaymentType(paymentOrderInfo.getPaymentType());
        refundOrderInfo.setRefundStatus(RefundStatusEnum.REFUNDING.getRefundStatus());
        refundOrderInfo.setRefundAmount(paymentOrderInfo.getTotalAmount());
        refundOrderInfo.setRefundReason(refundRequest.getRefundReason());
        refundOrderInfo.setNotifyUrl(createRefundNotifyUrl());
        refundOrderInfo.setRemark("");
        refundOrderInfo.setCreatedAt(DatetimeUtils.now());
        refundOrderInfo.setUpdatedAt(refundOrderInfo.getCreatedAt());
        refundOrderInfo.setDelFlag(0);
        paymentRefundOrderInfoService.insert(refundOrderInfo);
        // 调用对方退款接口
        PaymentTypeRefundParam param = null;
        try {
            param = paymentTypeRefund(refundOrderInfo, paymentOrderInfo);
        } catch (Exception e) {
            // 更新失败状态
            PaymentRefundOrderInfo updateInfo = new PaymentRefundOrderInfo();
            updateInfo.setId(paymentOrderInfo.getId());
            updateInfo.setRefundStatus(RefundStatusEnum.REFUND_FAILED.getRefundStatus());
            updateInfo.setUpdatedAt(DatetimeUtils.now());
            paymentRefundOrderInfoService.updateById(updateInfo);
            throw e;
        }
        // 更新外部订单号
        PaymentRefundOrderInfo updateInfo = new PaymentRefundOrderInfo();
        updateInfo.setId(refundOrderInfo.getId());
        updateInfo.setOutOrderId(param.getOutOrderId());
        updateInfo.setUpdatedAt(DatetimeUtils.now());
        paymentRefundOrderInfoService.updateById(updateInfo);
    }

    @Override
    public QueryRefundResultResponse queryRefundResult(PaymentRefundOrderInfo refundOrder) {
        QueryRefundResultResponse refundResultResponse = new QueryRefundResultResponse();
        if (RefundStatusEnum.isFinalStatus(refundOrder.getRefundStatus())) {
            refundResultResponse.setResult(refundOrder.getRefundStatus());
            refundResultResponse.setRemark(refundOrder.getRefundStatusRemark());
        } else {
            refundResultResponse = queryPaymentTypeRefundResult(refundOrder);
        }
        return refundResultResponse;
    }

    @Override
    public String dealPayFeedback(HttpServletRequest request) throws IOException {
        FeedbackParam feedbackParam = dealPayFeedbackRequest(request);
        if (null != feedbackParam.getResult()) {
            String key = Constants.ORDER_FEEDBACK_PREFIX + feedbackParam.getPaymentOrderId();
            String value = UUID.randomUUID().toString();
            boolean isLocked = false;
            try {
                isLocked = redisUtil.lock(key, value, 2, TimeUnit.SECONDS);
                if (isLocked) {
                    updatePaymentOrderInfo(feedbackParam);
                } else {
                    log.info("支付通知加锁失败");
                }
            } finally {
                if (isLocked) {
                    boolean isUnLock = redisUtil.unLock(key, value);
                    if (!isUnLock) {
                        log.info("支付通知解锁失败");
                    }
                }
            }
        }
        return feedbackParam.getResponseStr();
    }

    @Override
    public String dealRefundFeedback(HttpServletRequest request) throws IOException {
        FeedbackParam feedbackParam = dealRefundFeedbackRequest(request);
        if (null != feedbackParam.getResult()) {
            // 这里的paymentOrderId是退款订单refundOrderId
            String key = Constants.REFUND_FEEDBACK_PREFIX + feedbackParam.getPaymentOrderId();
            String value = UUID.randomUUID().toString();
            boolean isLocked = false;
            try {
                isLocked = redisUtil.lock(key, value, 2, TimeUnit.SECONDS);
                if (isLocked) {
                    updatePaymentRefundOrderInfo(feedbackParam);
                } else {
                    log.info("退款通知加锁失败");
                }
            } finally {
                if (isLocked) {
                    boolean isUnLock = redisUtil.unLock(key, value);
                    if (!isUnLock) {
                        log.info("退款通知解锁失败");
                    }
                }
            }
        }
        return feedbackParam.getResponseStr();
    }

    protected String createExtendInfo(String appId) {
        PaymentOrderExtendInfo extendInfo = new PaymentOrderExtendInfo();
        extendInfo.setAppId(appId);
        return JackJsonUtil.toJson(extendInfo);
    }

    protected void saveOrderLogInfo(String requestLog, String responseLog,
                                    PaymentOrderInfo paymentOrderInfo, PaymentLogTypeEnum logTypeEnum,
                                    long startTime, String msg) {
        PaymentOrderLogInfo paymentOrderLogInfo = new PaymentOrderLogInfo();
        paymentOrderLogInfo.setLogType(logTypeEnum.getLogType());
        paymentOrderLogInfo.setArtOrderId(paymentOrderInfo.getArtOrderId());
        paymentOrderLogInfo.setPaymentOrderId(paymentOrderInfo.getPaymentOrderId());
        paymentOrderLogInfo.setOutOrderId(paymentOrderInfo.getOutOrderId());
        paymentOrderLogInfo.setRequestStatus(!StringUtils.isEmpty(msg) ? 2 : 1);
        paymentOrderLogInfo.setCostTime(System.currentTimeMillis() - startTime);
        paymentOrderLogInfo.setRequestLog(requestLog);
        paymentOrderLogInfo.setResponseLog(!StringUtils.isEmpty(msg) ? msg : responseLog);
        paymentOrderLogInfo.setCreatedAt(DatetimeUtils.now());
        paymentOrderLogInfo.setUpdatedAt(paymentOrderLogInfo.getCreatedAt());
        paymentOrderLogInfo.setDelFlag(0);
//        paymentOrderLogInfoService.insert(paymentOrderLogInfo);
    }

    protected abstract String createPrePayNotifyUrl();

    protected abstract String createRefundNotifyUrl();

    protected abstract FeedbackParam dealPayFeedbackRequest(HttpServletRequest request) throws IOException;

    protected abstract FeedbackParam dealRefundFeedbackRequest(HttpServletRequest request) throws IOException;

    protected abstract PrePayResponseParams paymentTypePrePay(PrePayRequest prePayRequest, PaymentOrderInfo paymentOrderInfo);

    protected abstract PaymentTypeRefundParam paymentTypeRefund(PaymentRefundOrderInfo refundOrderInfo, PaymentOrderInfo paymentOrderInfo);

    protected abstract QueryRefundResultResponse queryPaymentTypeRefundResult(PaymentRefundOrderInfo refundOrder);

    private void updatePaymentOrderInfo(FeedbackParam feedbackParam) {
        Wrapper wrapper = Condition.create()
                .eq("payment_order_id", feedbackParam.getPaymentOrderId())
                .and().eq("del_flag", 0);
        PaymentOrderInfo paymentOrderInfo = paymentOrderInfoService.selectOne(wrapper);
        if (!PaymentStatusEnum.isFinalStatus(paymentOrderInfo.getPaymentStatus())) {
            PaymentOrderInfo updateInfo = new PaymentOrderInfo();
            updateInfo.setId(paymentOrderInfo.getId());
            updateInfo.setPaymentStatus(feedbackParam.getResult());
            updateInfo.setPaySuccessTime(feedbackParam.getSuccessTime());
            updateInfo.setPaymentStatusRemark(feedbackParam.getRemark());
            updateInfo.setUpdatedAt(DatetimeUtils.now());
            paymentOrderInfoService.updateById(updateInfo);
            paymentStatusChanged(paymentOrderInfo, feedbackParam.getResult());
        }
    }

    private void updatePaymentRefundOrderInfo(FeedbackParam feedbackParam) {
        Wrapper wrapper = Condition.create()
                .eq("refund_order_id", feedbackParam.getPaymentOrderId())
                .and().eq("del_flag", 0);
        PaymentRefundOrderInfo refundOrderInfo = paymentRefundOrderInfoService.selectOne(wrapper);
        if (!RefundStatusEnum.isFinalStatus(refundOrderInfo.getRefundStatus())) {
            PaymentRefundOrderInfo updateInfo = new PaymentRefundOrderInfo();
            updateInfo.setId(refundOrderInfo.getId());
            updateInfo.setRefundStatus(feedbackParam.getResult());
            updateInfo.setRefundSuccessTime(feedbackParam.getSuccessTime());
            updateInfo.setRefundStatusRemark(feedbackParam.getRemark());
            updateInfo.setUpdatedAt(DatetimeUtils.now());
            paymentRefundOrderInfoService.updateById(updateInfo);
            refundStatusChanged(refundOrderInfo, feedbackParam.getResult());
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
