package com.art.app.payment.enums;

import com.art.app.common.enums.PaymentStatusEnum;

import java.util.Arrays;
import java.util.Objects;

public enum WechatTradeStatusEnum {

    SUCCESS("SUCCESS", "支付成功"),
    REFUND("REFUND", "转入退款"),
    NOTPAY("NOTPAY", "未支付"),
    CLOSED("CLOSED", "已关闭"),
    REVOKED("REVOKED", "已撤销（刷卡支付）"),
    USERPAYING("USERPAYING", "用户支付中"),
    PAYERROR("PAYERROR", "支付失败(其他原因，如银行返回失败)"),
    ;

    private String orderStatus;
    private String desc;

    WechatTradeStatusEnum(String orderStatus, String desc) {
        this.orderStatus = orderStatus;
        this.desc = desc;
    }

    public static WechatTradeStatusEnum convertByOrderStatus(String orderStatus) {
        return Arrays.stream(WechatTradeStatusEnum.values()).filter(statusEnum -> {
            return Objects.equals(statusEnum.getOrderStatus(), orderStatus);
        }).findFirst().orElse(null);
    }

    public static PaymentStatusEnum convertWechatTradeStatusEnum(WechatTradeStatusEnum wechatTradeStatusEnum) {
        PaymentStatusEnum statusEnum = null;
        if (SUCCESS.equals(wechatTradeStatusEnum)) {
            // 支付成功
            statusEnum = PaymentStatusEnum.PAY_SUCCESS;
        } else if (NOTPAY.equals(wechatTradeStatusEnum) || USERPAYING.equals(wechatTradeStatusEnum)) {
            // 未支付
            statusEnum = PaymentStatusEnum.PAYING;
        } else if (PAYERROR.equals(wechatTradeStatusEnum)) {
            // 支付失败
            statusEnum = PaymentStatusEnum.PAY_FAILED;
        } else {
            statusEnum = PaymentStatusEnum.UNKNOWN;
        }
        return statusEnum;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getDesc() {
        return desc;
    }
}
