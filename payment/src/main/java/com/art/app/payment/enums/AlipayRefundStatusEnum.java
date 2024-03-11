package com.art.app.payment.enums;

import java.util.Arrays;
import java.util.Objects;

public enum AlipayRefundStatusEnum {

    SUCCESS("SUCCESS", "退款成功"),
    REFUNDCLOSE("REFUNDCLOSE", "退款关闭"),
    PROCESSING("PROCESSING", "退款处理中"),
    CHANGE("CHANGE", "退款异常"),
    ;

    private String orderStatus;
    private String desc;

    AlipayRefundStatusEnum(String orderStatus, String desc) {
        this.orderStatus = orderStatus;
        this.desc = desc;
    }

    public static AlipayRefundStatusEnum convertByOrderStatus(String orderStatus) {
        return Arrays.stream(AlipayRefundStatusEnum.values()).filter(statusEnum -> {
            return Objects.equals(statusEnum.getOrderStatus(), orderStatus);
        }).findFirst().orElse(null);
    }

    public static RefundStatusEnum convertAlipayRefundStatusEnum(AlipayRefundStatusEnum alipayRefundStatusEnum) {
        RefundStatusEnum statusEnum = null;
        if (SUCCESS.equals(alipayRefundStatusEnum) || REFUNDCLOSE.equals(alipayRefundStatusEnum)) {
            // 退款成功
            statusEnum = RefundStatusEnum.REFUND_SUCCESS;
        } else if (PROCESSING.equals(alipayRefundStatusEnum)) {
            // 退款中
            statusEnum = RefundStatusEnum.REFUNDING;
        } else if (CHANGE.equals(alipayRefundStatusEnum)) {
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
