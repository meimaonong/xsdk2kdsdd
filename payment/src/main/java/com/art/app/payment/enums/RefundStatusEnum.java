package com.art.app.payment.enums;

import java.util.Arrays;

public enum RefundStatusEnum {

    UNKNOWN(-1, "未知"),
    REFUNDING(0, "退款中"),
    REFUND_SUCCESS(1, "退款成功"),
    REFUND_FAILED(2, "退款失败"),
    ;

    private Integer refundStatus;
    private String desc;

    RefundStatusEnum(Integer refundStatus, String desc) {
        this.refundStatus = refundStatus;
        this.desc = desc;
    }

    public static RefundStatusEnum valueOf(int refundStatus) {
        return Arrays.stream(RefundStatusEnum.values()).filter(statusEnum -> {
            return statusEnum.getRefundStatus() == refundStatus;
        }).findFirst().orElse(UNKNOWN);
    }

    public static boolean isFinalStatus(int orderStatus) {
        RefundStatusEnum statusEnum = valueOf(orderStatus);
        return RefundStatusEnum.REFUND_SUCCESS.equals(statusEnum)
                || RefundStatusEnum.REFUND_FAILED.equals(statusEnum);
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public String getDesc() {
        return desc;
    }
}
