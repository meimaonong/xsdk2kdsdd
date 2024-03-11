package com.art.app.payment.enums;

public enum WechatPayUrlEnum {

    UNIFIED_ORDER("https://api.mch.weixin.qq.com/pay/unifiedorder", "统一下单"),
    ORDER_QUERY("https://api.mch.weixin.qq.com/pay/orderquery", "查询订单"),
    REFUND("https://api.mch.weixin.qq.com/secapi/pay/refund", "申请退款"),
    REFUND_QUERY("https://api.mch.weixin.qq.com/pay/refundquery", "查询退款"),
    ;
    private String url;
    private String desc;

    WechatPayUrlEnum(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public String getDesc() {
        return desc;
    }

}
