package com.art.app.payment.service.impl;

import com.art.app.common.enums.PaymentStatusEnum;
import com.art.app.common.enums.PaymentTypeEnum;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.common.util.NetworkUtil;
import com.art.app.common.util.XmlUtils;
import com.art.app.orm.entity.PaymentOrderInfo;
import com.art.app.orm.entity.PaymentRefundOrderInfo;
import com.art.app.orm.service.PaymentOrderInfoService;
import com.art.app.orm.service.PaymentRefundOrderInfoService;
import com.art.app.payment.PaymentException;
import com.art.app.payment.client.WechatCustomerClient;
import com.art.app.payment.client.config.WechatPayConfig;
import com.art.app.payment.entity.FeedbackParam;
import com.art.app.payment.entity.PaymentTypeRefundParam;
import com.art.app.payment.entity.PrePayRequest;
import com.art.app.payment.entity.QueryPayResultResponse;
import com.art.app.payment.entity.QueryRefundResultResponse;
import com.art.app.payment.entity.wechat.RequestLogParams;
import com.art.app.payment.entity.wechat.WechatPayFeedbackRequest;
import com.art.app.payment.entity.wechat.WechatPayFeedbackResponse;
import com.art.app.payment.entity.wechat.WechatPrePayResponseParams;
import com.art.app.payment.entity.wechat.WechatQueryPayResultRequest;
import com.art.app.payment.entity.wechat.WechatQueryPayResultResponse;
import com.art.app.payment.entity.wechat.WechatQueryRefundResultRequest;
import com.art.app.payment.entity.wechat.WechatQueryRefundResultResponse;
import com.art.app.payment.entity.wechat.WechatRefundFeedbackRequest;
import com.art.app.payment.entity.wechat.WechatRefundFeedbackRequestDetail;
import com.art.app.payment.entity.wechat.WechatRefundFeedbackResponse;
import com.art.app.payment.entity.wechat.WechatRefundRequest;
import com.art.app.payment.entity.wechat.WechatRefundResponse;
import com.art.app.payment.entity.wechat.WechatUnifiedOrderRequest;
import com.art.app.payment.entity.wechat.WechatUnifiedOrderResponse;
import com.art.app.payment.enums.PaymentErrorCodeEnum;
import com.art.app.payment.enums.PaymentLogTypeEnum;
import com.art.app.payment.enums.RefundStatusEnum;
import com.art.app.payment.enums.WechatPayUrlEnum;
import com.art.app.payment.enums.WechatRefundStatusEnum;
import com.art.app.payment.enums.WechatTradeStatusEnum;
import com.art.app.payment.service.AbstractPaymentTypeService;
import com.art.app.payment.utils.JackJsonUtil;
import com.art.app.payment.utils.WechatUtil;
import com.art.app.payment.utils.wechat.WXPayUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Map;

@Service
@Slf4j
public class WechatPaymentServiceImpl extends AbstractPaymentTypeService {

    @Value("${local.host}")
    private String localHost;
    @Resource
    private WechatCustomerClient wechatCustomerClient;
    @Resource
    private PaymentOrderInfoService paymentOrderInfoService;
    @Resource
    private PaymentRefundOrderInfoService paymentRefundOrderInfoService;

    @Override
    public boolean matching(Integer paymentType) {
        return PaymentTypeEnum.WECHAT.getType() == paymentType;
    }

    @Override
    protected WechatPrePayResponseParams paymentTypePrePay(PrePayRequest prePayRequest, PaymentOrderInfo paymentOrderInfo) {
        WechatUnifiedOrderRequest unifiedOrderRequest = new WechatUnifiedOrderRequest();
        unifiedOrderRequest.setBody(prePayRequest.getSubject());
        unifiedOrderRequest.setOutTradeNo(paymentOrderInfo.getPaymentOrderId());
        unifiedOrderRequest.setTotalFee(convertPrice(prePayRequest.getAmount()));
        unifiedOrderRequest.setSpbillCreateIp(NetworkUtil.getLinuxServerIp());
        unifiedOrderRequest.setNotifyUrl(paymentOrderInfo.getNotifyUrl());
        unifiedOrderRequest.setTradeType("APP");
        unifiedOrderRequest.setTimeExpire(DatetimeUtils.formatDatetime(paymentOrderInfo.getTimeExpire(), DatetimeUtils.YYYYMMDDHHMMSS));
        WechatUnifiedOrderResponse orderResponse = null;
        RequestLogParams requestLogParams = new RequestLogParams();
        long startTime = System.currentTimeMillis();
        try {
            orderResponse = wechatCustomerClient.process(unifiedOrderRequest,
                    WechatPayUrlEnum.UNIFIED_ORDER, WechatUnifiedOrderResponse.class, requestLogParams);
        } finally {
            saveOrderLogInfo(requestLogParams, paymentOrderInfo, PaymentLogTypeEnum.WECHAT_PRE_PAY, startTime);
        }
        // 组装唤醒参数
        WechatPrePayResponseParams responseParams = convertPaymentTypePrePayParam(orderResponse);
        // 更新OrderString
        PaymentOrderInfo updateInfo = new PaymentOrderInfo();
        updateInfo.setId(paymentOrderInfo.getId());
        updateInfo.setOrderString(JackJsonUtil.toJson(responseParams));
        updateInfo.setUpdatedAt(DatetimeUtils.now());
        updateInfo.setExtendInfo(createExtendInfo(WechatPayConfig.getWechatAppId()));
        paymentOrderInfoService.updateById(updateInfo);
        return responseParams;
    }

    private void saveOrderLogInfo(RequestLogParams requestLogParams, PaymentOrderInfo paymentOrderInfo, PaymentLogTypeEnum logTypeEnum, long startTime) {
        String msg = requestLogParams.isResponseError() ? requestLogParams.getResponseParam() : "";
        saveOrderLogInfo(requestLogParams.getRequestParam(), requestLogParams.getResponseParam(), paymentOrderInfo, logTypeEnum, startTime, msg);
    }

    @Override
    protected PaymentTypeRefundParam paymentTypeRefund(PaymentRefundOrderInfo refundOrderInfo, PaymentOrderInfo paymentOrderInfo) {
        WechatRefundRequest refundRequest = new WechatRefundRequest();
        refundRequest.setTransactionId(paymentOrderInfo.getOutOrderId());
        refundRequest.setOutTradeNo(paymentOrderInfo.getPaymentOrderId());
        refundRequest.setOutRefundNo(refundOrderInfo.getRefundOrderId());
        refundRequest.setTotalFee(convertPrice(paymentOrderInfo.getTotalAmount()));
        refundRequest.setRefundFee(refundRequest.getTotalFee());
        refundRequest.setRefundDesc(refundOrderInfo.getRefundReason());
        refundRequest.setNotifyUrl(refundOrderInfo.getNotifyUrl());
        RequestLogParams requestLogParams = new RequestLogParams();
        WechatRefundResponse refundResponse = null;
        long startTime = System.currentTimeMillis();
        try {
            refundResponse = wechatCustomerClient.process(refundRequest,
                    WechatPayUrlEnum.REFUND, WechatRefundResponse.class, requestLogParams);
        } finally {
            saveOrderLogInfo(requestLogParams, paymentOrderInfo, PaymentLogTypeEnum.WECHAT_PRE_PAY, startTime);
        }
        PaymentTypeRefundParam result = new PaymentTypeRefundParam();
        result.setOutOrderId(refundResponse.getRefundId());
        return result;
    }

    @Override
    public QueryPayResultResponse queryPaymentTypePayResult(PaymentOrderInfo paymentOrderInfo) {
        WechatQueryPayResultRequest payResultRequest = new WechatQueryPayResultRequest();
        payResultRequest.setTransactionId(paymentOrderInfo.getOutOrderId());
        payResultRequest.setOutTradeNo(paymentOrderInfo.getPaymentOrderId());
        WechatQueryPayResultResponse payResultResponse = wechatCustomerClient.process(payResultRequest,
                WechatPayUrlEnum.ORDER_QUERY, WechatQueryPayResultResponse.class);
        QueryPayResultResponse response = new QueryPayResultResponse();
        WechatTradeStatusEnum statusEnum = WechatTradeStatusEnum.convertByOrderStatus(payResultResponse.getTradeState());
        PaymentStatusEnum paymentStatusEnum = WechatTradeStatusEnum.convertWechatTradeStatusEnum(statusEnum);
        if (PaymentStatusEnum.UNKNOWN.equals(paymentStatusEnum)) {
            throw new PaymentException(PaymentErrorCodeEnum.WECHAT_PAY_RESULT_UNKNOWN_STATUS);
        } else {
            response.setResult(paymentStatusEnum.getStatus());
            response.setRemark(paymentStatusEnum.getDesc());
            response.setOutOrderId(payResultResponse.getTransactionId());
            return response;
        }
    }

    @Override
    protected QueryRefundResultResponse queryPaymentTypeRefundResult(PaymentRefundOrderInfo refundOrder) {
        WechatQueryRefundResultRequest refundResultRequest = new WechatQueryRefundResultRequest();
//        refundResultRequest.setTransactionId(refundOrder.getPaymentOrderId());
//        refundResultRequest.setOutTradeNo(refundOrder.getPaymentOrderId());
        refundResultRequest.setOutRefundNo(refundOrder.getRefundOrderId());
        refundResultRequest.setRefundId(refundOrder.getOutOrderId());
        WechatQueryRefundResultResponse refundResultResponse = wechatCustomerClient.process(refundResultRequest,
                WechatPayUrlEnum.REFUND_QUERY, WechatQueryRefundResultResponse.class);
        QueryRefundResultResponse response = new QueryRefundResultResponse();
        WechatRefundStatusEnum statusEnum = WechatRefundStatusEnum.convertByOrderStatus(refundResultResponse.getRefundStatus0());
        RefundStatusEnum refundStatusEnum = WechatRefundStatusEnum.convertWechatRefundStatusEnum(statusEnum);
        if (RefundStatusEnum.UNKNOWN.equals(refundStatusEnum)) {
            throw new PaymentException(PaymentErrorCodeEnum.WECHAT_REFUND_RESPONSE_UNKNOWN_STATUS);
        } else {
            response.setResult(refundStatusEnum.getRefundStatus());
            response.setRemark(refundStatusEnum.getDesc());
            return response;
        }
    }

    @Override
    protected String createPrePayNotifyUrl() {
        return localHost + "/payment/feedback/wechat/notify";
    }

    @Override
    protected String createRefundNotifyUrl() {
        return localHost + "/payment/feedback/wechat/refund/notify";
    }

    @Override
    public FeedbackParam dealPayFeedbackRequest(HttpServletRequest request) throws IOException {
        FeedbackParam result = new FeedbackParam();
        WechatPayFeedbackResponse responseParams = new WechatPayFeedbackResponse();
        String notifyParam = null;
        try {
            notifyParam = convertFeedbackParam(request);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            responseParams.setReturnCode("FAIL");
            responseParams.setReturnMsg(e.getMessage());
            result.setResponseStr(JackJsonUtil.toJson(responseParams));
            return result;
        }
        WechatPayFeedbackRequest feedbackRequest = JackJsonUtil.toBean(notifyParam,
                new TypeReference<WechatPayFeedbackRequest>() {
                });
        result.setPaymentOrderId(feedbackRequest.getOutTradeNo());
        result.setOutOrderId(feedbackRequest.getTransactionId());
        if ("SUCCESS".equals(feedbackRequest.getResultCode())) {
            result.setResult(PaymentStatusEnum.PAY_SUCCESS.getStatus());
            result.setSuccessTime(DatetimeUtils.parseDatetime(feedbackRequest.getTimeEnd(), DatetimeUtils.YYYYMMDDHHMMSS));
        } else {
            log.info("微信支付反馈 -> 失败 : " + feedbackRequest.getErrCode());
            result.setResult(PaymentStatusEnum.PAY_FAILED.getStatus());
            result.setRemark(feedbackRequest.getErrCode() + "-" + feedbackRequest.getErrCodeDes());
        }
        responseParams.setReturnCode("SUCCESS");
        responseParams.setReturnMsg("OK");
        result.setResponseStr(XmlUtils.object2Xml(responseParams));
        return result;
    }

    @Override
    protected FeedbackParam dealRefundFeedbackRequest(HttpServletRequest request) throws IOException {
        FeedbackParam result = new FeedbackParam();
        WechatRefundFeedbackResponse responseParams = new WechatRefundFeedbackResponse();
        String notifyParam = null;
        try {
            notifyParam = convertFeedbackParam(request);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            responseParams.setReturnCode("FAIL");
            responseParams.setReturnMsg(e.getMessage());
            result.setResponseStr(JackJsonUtil.toJson(responseParams));
            return result;
        }
        WechatRefundFeedbackRequest feedbackRequest = JackJsonUtil.toBean(notifyParam,
                new TypeReference<WechatRefundFeedbackRequest>() {
                });
        String reqInfo = feedbackRequest.getReqInfo();
        WechatRefundFeedbackRequestDetail detail = decryptReqInfo(reqInfo);
        result.setPaymentOrderId(detail.getOutTradeNo());
        result.setOutOrderId(detail.getTransactionId());
        WechatRefundStatusEnum statusEnum = WechatRefundStatusEnum.convertByOrderStatus(detail.getRefundStatus());
        RefundStatusEnum refundStatusEnum = WechatRefundStatusEnum.convertWechatRefundStatusEnum(statusEnum);
        result.setResult(refundStatusEnum.getRefundStatus());
        result.setSuccessTime(detail.getSuccessTime());
        result.setRemark(refundStatusEnum.getDesc());
        responseParams.setReturnCode("SUCCESS");
        responseParams.setReturnMsg("OK");
        result.setResponseStr(XmlUtils.object2Xml(responseParams));
        return result;
    }

    private Integer convertPrice(BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(100L)).intValue();
    }

    private String convertFeedbackParam(HttpServletRequest request) throws Exception {
        StringBuilder param = new StringBuilder();
        String str = null;
        try (InputStream inputStream = request.getInputStream();
             BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));) {
            while ((str = in.readLine()) != null) {
                param.append(str);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        log.info("微信异步通知参数 -> {}", param.toString());
        //解析xml验签
        Map<String, String> data = WXPayUtil.xmlToMap(param.toString());
        if (!WXPayUtil.isSignatureValid(data, WechatPayConfig.getWechatApiKey())) {
            log.info("微信异步通知 -> 签名验证失败");
            throw new PaymentException(PaymentErrorCodeEnum.WECHAT_PAY_RESULT_FEEDBACK_SIGN_ERROR);
        }
        return JackJsonUtil.toJson(data);
    }

    private WechatRefundFeedbackRequestDetail decryptReqInfo(String reqInfo) {
        String decrypt = WechatUtil.decryptReqInfo(reqInfo, WechatPayConfig.getWechatApiKey());
        WechatRefundFeedbackRequestDetail detail = XmlUtils.xml2Object(decrypt, WechatRefundFeedbackRequestDetail.class);
        log.info("reqInfo -> {}, {}", decrypt, JackJsonUtil.toJson(detail));
        return detail;
    }

    private WechatPrePayResponseParams convertPaymentTypePrePayParam(WechatUnifiedOrderResponse orderResponse) {
        WechatPrePayResponseParams orderString = new WechatPrePayResponseParams();
        orderString.setAppId(WechatPayConfig.getWechatAppId());
        orderString.setPartnerId(WechatPayConfig.getWechatMchId());
        orderString.setPrePayId(orderResponse.getPrepayId());
        orderString.setWechatPackage("Sign=WXPay");
        orderString.setNonceStr(WechatUtil.createNonceStr());
        orderString.setTimestamp(String.valueOf(System.currentTimeMillis() / 1000));
        orderString.setSign(createSign(orderString));
        return orderString;
    }

    private String createSign(WechatPrePayResponseParams requestParam) {
        Map<String, Object> requestMap = JackJsonUtil.toBean(JackJsonUtil.toJson(requestParam), new TypeReference<Map<String, Object>>() {
        });
        return WechatUtil.sign(requestMap, WechatPayConfig.getWechatApiKey());
    }

    public static void main(String[] args) {
        WechatPrePayResponseParams orderString = new WechatPrePayResponseParams();
        orderString.setAppId("wx8e22176dc162a64b");
        orderString.setPartnerId("1493825602");
        orderString.setPrePayId("wx042208235994056d2eb954851490755900");
        orderString.setWechatPackage("Sign=WXPay");
        orderString.setNonceStr("05fcc65ac9684de6b14d1beb1bfaf794");
        orderString.setTimestamp("1591279703");
        Map<String, Object> requestMap = JackJsonUtil.toBean(JackJsonUtil.toJson(orderString), new TypeReference<Map<String, Object>>() {
        });
        orderString.setSign(WechatUtil.sign(requestMap, WechatPayConfig.getWechatApiKey()));
        System.out.println(JackJsonUtil.toJson(orderString));
    }
}
