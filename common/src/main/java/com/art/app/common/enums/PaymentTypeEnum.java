package com.art.app.common.enums;

import lombok.Getter;

@Getter
public enum PaymentTypeEnum {
    UNKNOWN(0, "未知"),
    ALIPAY(1, "支付宝"),
    WECHAT(2, "微信"),
    ;

    private int type;
    private String name;

    PaymentTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static PaymentTypeEnum ofType(int type) {
        for (PaymentTypeEnum value : PaymentTypeEnum.values()) {
            if (type == value.getType()) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
