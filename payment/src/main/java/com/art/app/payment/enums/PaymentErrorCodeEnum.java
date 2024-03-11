package com.art.app.payment.enums;

import com.art.app.common.bean.ErrorCode;

import java.util.Arrays;

public enum PaymentErrorCodeEnum implements ErrorCode {

    SUCCESS(400000, "成功"),
    PAYMENT_ORDER_NOT_EXIST(400001, "支付订单不存在"),
    REFUND_ORDER_NOT_EXIST(400002, "退款订单不存在"),
    OUT_TRADE_NO_NOT_EXIST(400003, "外系统返回订单不存在"),

    ALIPAY_PRE_PAY_REQUEST_ERROR(401101, "请求支付宝预支付接口失败"),
    ALIPAY_QUERY_PAY_RESULT_ERROR(401102, "请求支付宝查询支付结果接口失败"),
    ALIPAY_PAY_RESULT_UNKNOWN_STATUS(401103, "支付宝支付接口返回未知状态"),
    ALIPAY_PAY_RESULT_FEEDBACK_SIGN_ERROR(401104, "支付宝支付结果异步通知验签失败"),
    ALIPAY_REQUEST_ERROR(401105, "请求支付宝接口失败"),

    ALIPAY_REFUND_RESPONSE_UNKNOWN_STATUS(401201, "支付宝退款接口返回未知状态"),
    ALIPAY_REFUND_RESULT_QUERY_ERROR(401202, "支付宝查询退款结果接口失败"),
    ALIPAY_REFUND_REQUEST_ERROR(401203, "支付宝退款接口请求失败"),

    WECHAT_PAY_REQUEST_ERROR(402101, "请求微信接口失败"),
    WECHAT_PAY_RESPONSE_XML_ERROR(402102, "微信接口返回数据XML解析失败"),
    WECHAT_PAY_RESPONSE_RETURN_CODE_ERROR(402103, "微信接口返回错误码失败"),
    WECHAT_PAY_RESPONSE_SIGN_ERROR(402104, "微信接口返回数据验签失败"),
    WECHAT_PAY_RESPONSE_RESULT_CODE_ERROR(402105, "微信接口返回业务处理失败"),
    WECHAT_PAY_RESULT_UNKNOWN_STATUS(402106, "微信支付接口返回未知状态"),
    WECHAT_PAY_RESULT_FEEDBACK_SIGN_ERROR(402107, "微信支付结果异步通知验签失败"),
    WECHAT_PAY_RESULT_FEEDBACK_DATA_ERROR(402108, "微信支付结果异步通知数据转换失败"),

    WECHAT_REFUND_RESPONSE_UNKNOWN_STATUS(402201, "微信退款接口返回未知状态"),



    SYSTEM_ERROR(499999, "系统错误，请稍后重试"),
    ;
    private final int code;
    private final String msg;

    PaymentErrorCodeEnum(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public static PaymentErrorCodeEnum valueOf(int code) {
        return Arrays.stream(PaymentErrorCodeEnum.values()).filter(paymentErrorCodeEnum -> {
            return paymentErrorCodeEnum.getCode() == code;
        }).findFirst().orElse(SYSTEM_ERROR);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
