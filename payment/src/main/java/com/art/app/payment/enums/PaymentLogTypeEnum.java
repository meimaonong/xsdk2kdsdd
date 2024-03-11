package com.art.app.payment.enums;

public enum PaymentLogTypeEnum {

    UNKNOWN(0, ""),
    ALI_PAY_PRE_PAY(1000, "支付宝预支付"),
    ALI_PAY_FEEDBACK(1001, "支付宝异步通知"),
    ALI_PAY_REFUND(1002, "支付宝退款"),
    WECHAT_PRE_PAY(2000, "微信预支付"),
    WECHAT_FEEDBACK(2001, "微信支付返回"),
    WECHAT_REFUND(2002, "微信支付退款"),
    ;

    private Integer logType;
    private String desc;

    PaymentLogTypeEnum(Integer logType, String desc) {
        this.logType = logType;
        this.desc = desc;
    }

    public Integer getLogType() {
        return logType;
    }

    public String getDesc() {
        return desc;
    }
}
