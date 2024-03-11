package com.art.app.common.enums;

import java.util.Arrays;

public enum OrderPaymentStatusEnum {

    UNKNOWN(-1, "未知"),
    NOT_PAID(1, "未支付"),
    PAYING(2, "支付中"),
    SUCCESS(3, "已支付"),
    CANCEL(4, "已取消"),
    FAILED(5, "支付失败"),
    REFUND(6, "已退款"),
    ;

    private Integer status;
    private String desc;

    OrderPaymentStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static OrderPaymentStatusEnum valueOf(int status) {
        return Arrays.stream(OrderPaymentStatusEnum.values()).filter(paymentStatusEnum -> {
            return paymentStatusEnum.getStatus() == status;
        }).findFirst().orElse(UNKNOWN);
    }

    public static boolean isFinalStatus(int status) {
        OrderPaymentStatusEnum statusEnum = valueOf(status);
        return OrderPaymentStatusEnum.SUCCESS.equals(statusEnum)
                || OrderPaymentStatusEnum.CANCEL.equals(statusEnum)
                || OrderPaymentStatusEnum.FAILED.equals(statusEnum);
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
