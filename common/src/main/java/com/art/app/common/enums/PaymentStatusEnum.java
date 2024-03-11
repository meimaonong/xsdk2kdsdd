package com.art.app.common.enums;

import java.util.Arrays;

public enum PaymentStatusEnum {

    UNKNOWN(-1, "未知"),
    PAYING(0, "支付中"),
    PAY_SUCCESS(1, "支付成功"),
    PAY_FAILED(2, "支付失败"),
    CANCELED(3, "支付取消"),
    ;

    private Integer status;
    private String desc;

    PaymentStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static PaymentStatusEnum valueOf(int status) {
        return Arrays.stream(PaymentStatusEnum.values()).filter(paymentStatusEnum -> {
            return paymentStatusEnum.getStatus() == status;
        }).findFirst().orElse(UNKNOWN);
    }

    public static boolean isFinalStatus(int status) {
        PaymentStatusEnum statusEnum = valueOf(status);
        return PaymentStatusEnum.PAY_SUCCESS.equals(statusEnum)
                || PaymentStatusEnum.PAY_FAILED.equals(statusEnum)
                || PaymentStatusEnum.CANCELED.equals(statusEnum);
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
