package com.art.app.payment.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.art.app.common.enums.PaymentStatusEnum;
import com.art.app.common.enums.PaymentTypeEnum;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.common.util.MathUtils;
import com.art.app.orm.entity.PaymentOrderInfo;
import com.art.app.orm.entity.PaymentRefundOrderInfo;
import com.art.app.orm.service.PaymentOrderInfoService;
import com.art.app.payment.PaymentException;
import com.art.app.payment.client.config.AlipayConfig;
import com.art.app.payment.client.config.WechatPayConfig;
import com.art.app.payment.entity.FeedbackParam;
import com.art.app.payment.entity.PaymentTypeRefundParam;
import com.art.app.payment.entity.PrePayRequest;
import com.art.app.payment.entity.QueryPayResultResponse;
import com.art.app.payment.entity.QueryRefundResultResponse;
import com.art.app.payment.entity.ali.AlipayFeedbackRequest;
import com.art.app.payment.entity.ali.AlipayPrePayResponseParams;
import com.art.app.payment.enums.AlipayRefundStatusEnum;
import com.art.app.payment.enums.AlipayTradeStatusEnum;
import com.art.app.payment.enums.PaymentErrorCodeEnum;
import com.art.app.payment.enums.PaymentLogTypeEnum;
import com.art.app.payment.enums.RefundStatusEnum;
import com.art.app.payment.service.AbstractPaymentTypeService;
import com.art.app.payment.utils.JackJsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AlipayPaymentServiceImpl extends AbstractPaymentTypeService {

    @Value("${local.host}")
    private String localHost;
    @Resource
    private PaymentOrderInfoService paymentOrderInfoService;

    @Override
    public boolean matching(Integer paymentType) {
        return PaymentTypeEnum.ALIPAY.getType() == paymentType;
    }

    @Override
    protected AlipayPrePayResponseParams paymentTypePrePay(PrePayRequest prePayRequest, PaymentOrderInfo paymentOrderInfo) {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSubject(paymentOrderInfo.getSubject());
        model.setOutTradeNo(paymentOrderInfo.getPaymentOrderId());
        model.setTotalAmount(MathUtils.format(paymentOrderInfo.getTotalAmount()));
        model.setTimeExpire(DatetimeUtils.formatDatetime(paymentOrderInfo.getTimeExpire(), DatetimeUtils.YYYY_MM_DD_HH_MM));
        request.setBizModel(model);
        request.setNotifyUrl(paymentOrderInfo.getNotifyUrl());
        AlipayTradeAppPayResponse alipayTradeAppPayResponse = null;
        long startTime = System.currentTimeMillis();
        String msg = "";
        try {
            alipayTradeAppPayResponse = AlipayConfig.getAlipayClientInstance().sdkExecute(request);
        } catch (AlipayApiException e) {
            msg = e.getErrMsg();
            throw new PaymentException(PaymentErrorCodeEnum.ALIPAY_PRE_PAY_REQUEST_ERROR, e);
        } finally {
            // 保存调用信息
            saveOrderLogInfo(request, alipayTradeAppPayResponse, paymentOrderInfo, PaymentLogTypeEnum.ALI_PAY_PRE_PAY, startTime, msg);
        }
        // 更新orderString
        PaymentOrderInfo updateInfo = new PaymentOrderInfo();
        updateInfo.setId(paymentOrderInfo.getId());
        updateInfo.setOrderString(alipayTradeAppPayResponse.getBody());
        updateInfo.setUpdatedAt(DatetimeUtils.now());
        updateInfo.setExtendInfo(createExtendInfo(WechatPayConfig.getWechatAppId()));
        paymentOrderInfoService.updateById(updateInfo);
        AlipayPrePayResponseParams result = new AlipayPrePayResponseParams();
        result.setOrderString(alipayTradeAppPayResponse.getBody());
        return result;
    }

    @Override
    public QueryPayResultResponse queryPaymentTypePayResult(PaymentOrderInfo paymentOrderInfo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel bizModel = new AlipayTradeQueryModel();
        bizModel.setOutTradeNo(paymentOrderInfo.getPaymentOrderId());
        bizModel.setTradeNo(paymentOrderInfo.getOutOrderId());
        request.setBizModel(bizModel);
        AlipayTradeQueryResponse tradeQueryResponse = null;
        try {
            tradeQueryResponse = AlipayConfig.getAlipayClientInstance().execute(request);
        } catch (AlipayApiException e) {
            throw new PaymentException(PaymentErrorCodeEnum.ALIPAY_QUERY_PAY_RESULT_ERROR, e);
        }
        AlipayTradeStatusEnum statusEnum = AlipayTradeStatusEnum.convertByOrderStatus(tradeQueryResponse.getTradeStatus());
        PaymentStatusEnum paymentStatusEnum = AlipayTradeStatusEnum.convertAlipayTradeStatusToPaymentStatus(statusEnum);
        if (PaymentStatusEnum.UNKNOWN.equals(paymentStatusEnum)) {
            throw new PaymentException(PaymentErrorCodeEnum.ALIPAY_PAY_RESULT_UNKNOWN_STATUS);
        } else {
            QueryPayResultResponse result = new QueryPayResultResponse();
            result.setResult(paymentStatusEnum.getStatus());
            result.setRemark(paymentStatusEnum.getDesc());
            result.setOutOrderId(tradeQueryResponse.getTradeNo());
            return result;
        }
    }

    @Override
    protected PaymentTypeRefundParam paymentTypeRefund(PaymentRefundOrderInfo refundOrderInfo, PaymentOrderInfo paymentOrderInfo) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(paymentOrderInfo.getPaymentOrderId());
        model.setTradeNo(paymentOrderInfo.getOutOrderId());
        model.setRefundAmount(MathUtils.format(paymentOrderInfo.getTotalAmount()));
        model.setRefundReason(refundOrderInfo.getRefundReason());
        request.setBizModel(model);
        AlipayTradeRefundResponse refundResponse = null;
        long startTime = System.currentTimeMillis();
        String msg = "";
        try {
            refundResponse = AlipayConfig.getAlipayClientInstance().execute(request);
        } catch (AlipayApiException e) {
            msg = e.getErrMsg();
            throw new PaymentException(PaymentErrorCodeEnum.ALIPAY_REFUND_REQUEST_ERROR);
        } finally {
            // 保存调用信息
            saveOrderLogInfo(request, refundResponse, paymentOrderInfo, PaymentLogTypeEnum.ALI_PAY_REFUND, startTime, msg);
        }
        PaymentTypeRefundParam result = new PaymentTypeRefundParam();
        result.setOutOrderId(refundResponse.getTradeNo());
        return result;
    }

    @Override
    protected QueryRefundResultResponse queryPaymentTypeRefundResult(PaymentRefundOrderInfo refundOrder) {
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
        model.setTradeNo(refundOrder.getOutOrderId());
        model.setOutTradeNo(refundOrder.getRefundOrderId());
        model.setOutRequestNo(refundOrder.getRefundOrderId());
        request.setBizModel(model);
        AlipayTradeFastpayRefundQueryResponse refundQueryResponse = null;
        try {
            refundQueryResponse = AlipayConfig.getAlipayClientInstance().execute(request);
        } catch (AlipayApiException e) {
            throw new PaymentException(PaymentErrorCodeEnum.ALIPAY_REFUND_RESULT_QUERY_ERROR);
        }
        AlipayRefundStatusEnum statusEnum = AlipayRefundStatusEnum.convertByOrderStatus(refundQueryResponse.getRefundStatus());
        RefundStatusEnum refundStatusEnum = AlipayRefundStatusEnum.convertAlipayRefundStatusEnum(statusEnum);
        if (RefundStatusEnum.UNKNOWN.equals(refundStatusEnum)) {
            throw new PaymentException(PaymentErrorCodeEnum.ALIPAY_REFUND_RESPONSE_UNKNOWN_STATUS);
        } else {
            QueryRefundResultResponse response = new QueryRefundResultResponse();
            response.setResult(refundStatusEnum.getRefundStatus());
            response.setRemark(refundStatusEnum.getDesc());
            return response;
        }
    }

    @Override
    protected String createPrePayNotifyUrl() {
        return localHost + "/payment/feedback/alipay/notify";
    }

    @Override
    protected String createRefundNotifyUrl() {
        return localHost + "/payment/feedback/alipay/refund/notify";
    }

    @Override
    public FeedbackParam dealPayFeedbackRequest(HttpServletRequest request) {
        AlipayFeedbackRequest feedbackRequest = convertFeedbackParam(request);
        FeedbackParam feedbackParam = new FeedbackParam();
        AlipayTradeStatusEnum statusEnum = AlipayTradeStatusEnum.convertByOrderStatus(feedbackRequest.getTradeStatus());
        PaymentStatusEnum paymentStatusEnum = AlipayTradeStatusEnum.convertAlipayTradeStatusToPaymentStatus(statusEnum);
        if (PaymentStatusEnum.UNKNOWN.equals(paymentStatusEnum)) {
            throw new PaymentException(PaymentErrorCodeEnum.ALIPAY_PAY_RESULT_UNKNOWN_STATUS);
        } else {
            feedbackParam.setResult(paymentStatusEnum.getStatus());
            feedbackParam.setRemark(paymentStatusEnum.getDesc());
            feedbackParam.setSuccessTime(feedbackRequest.getGmtPayment());
            feedbackParam.setOutOrderId(feedbackRequest.getTradeNo());
            feedbackParam.setPaymentOrderId(feedbackRequest.getOutTradeNo());
            return feedbackParam;
        }
    }

    @Override
    protected FeedbackParam dealRefundFeedbackRequest(HttpServletRequest request) {
        AlipayFeedbackRequest feedbackRequest = convertFeedbackParam(request);
        FeedbackParam feedbackParam = new FeedbackParam();
        AlipayTradeStatusEnum statusEnum = AlipayTradeStatusEnum.convertByOrderStatus(feedbackRequest.getTradeStatus());
        RefundStatusEnum refundStatusEnum = AlipayTradeStatusEnum.convertAlipayTradeStatusToRefundStatus(statusEnum);
        if (RefundStatusEnum.UNKNOWN.equals(refundStatusEnum)) {
            throw new PaymentException(PaymentErrorCodeEnum.ALIPAY_PAY_RESULT_UNKNOWN_STATUS);
        } else {
            feedbackParam.setResult(refundStatusEnum.getRefundStatus());
            feedbackParam.setRemark(refundStatusEnum.getDesc());
            feedbackParam.setSuccessTime(feedbackRequest.getGmtPayment());
            feedbackParam.setOutOrderId(feedbackRequest.getTradeNo());
            feedbackParam.setPaymentOrderId(feedbackRequest.getOutTradeNo());
            feedbackParam.setResponseStr("SUCCESS");
            return feedbackParam;
        }
    }

    private AlipayFeedbackRequest convertFeedbackParam(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        log.info("支付宝异步通知参数 -> {}", JackJsonUtil.toJson(params));
        boolean flag = AlipayConfig.verifySign(params);
        if (flag) {
            return JackJsonUtil.toBean(JackJsonUtil.toJson(params), new TypeReference<AlipayFeedbackRequest>() {
            });
        } else {
            log.info("支付宝异步通知 -> 签名验证失败");
            throw new PaymentException(PaymentErrorCodeEnum.ALIPAY_PAY_RESULT_FEEDBACK_SIGN_ERROR);
        }
    }

    private <T extends AlipayResponse> void saveOrderLogInfo(AlipayRequest<T> request, T response,
                                                             PaymentOrderInfo paymentOrderInfo, PaymentLogTypeEnum logTypeEnum,
                                                             long startTime, String msg) {
        saveOrderLogInfo(JackJsonUtil.toJson(request), JackJsonUtil.toJson(response), paymentOrderInfo, logTypeEnum, startTime, msg);
    }
}
