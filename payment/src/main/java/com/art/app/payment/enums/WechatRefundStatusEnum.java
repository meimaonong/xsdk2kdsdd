package com.art.app.payment.enums;

import java.util.Arrays;
import java.util.Objects;

public enum WechatRefundStatusEnum {

    SUCCESS("SUCCESS", "退款成功"),
    REFUNDCLOSE("REFUNDCLOSE", "退款关闭"),
    PROCESSING("PROCESSING", "退款处理中"),
    CHANGE("CHANGE", "退款异常"),
    ;

    private String orderStatus;
    private String desc;

    WechatRefundStatusEnum(String orderStatus, String desc) {
        this.orderStatus = orderStatus;
        this.desc = desc;
    }

    public static WechatRefundStatusEnum convertByOrderStatus(String orderStatus) {
        return Arrays.stream(WechatRefundStatusEnum.values()).filter(statusEnum -> {
            return Objects.equals(statusEnum.getOrderStatus(), orderStatus);
        }).findFirst().orElse(null);
    }

    public static RefundStatusEnum convertWechatRefundStatusEnum(WechatRefundStatusEnum wechatRefundStatusEnum) {
        RefundStatusEnum statusEnum = null;
        if (SUCCESS.equals(wechatRefundStatusEnum) || REFUNDCLOSE.equals(wechatRefundStatusEnum)) {
            // 退款成功
            statusEnum = RefundStatusEnum.REFUND_SUCCESS;
        } else if (PROCESSING.equals(wechatRefundStatusEnum)) {
            // 退款中
            statusEnum = RefundStatusEnum.REFUNDING;
        } else if (CHANGE.equals(wechatRefundStatusEnum)) {
            // 退款失败
            statusEnum = RefundStatusEnum.REFUND_FAILED;
        } else {
            statusEnum = RefundStatusEnum.UNKNOWN;
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
