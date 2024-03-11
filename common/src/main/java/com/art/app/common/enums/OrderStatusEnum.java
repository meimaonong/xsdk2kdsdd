package com.art.app.common.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {

    UNKNOWN(0, "未知"),
    APPLYING(1, "处理中"),
    APPLY_SUCCESS(2, "处理成功"),
    APPLY_FAIL(3, "处理失败"),;

    private int type;
    private String name;

    OrderStatusEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static OrderStatusEnum ofType(int type) {
        for (OrderStatusEnum value : OrderStatusEnum.values()) {
            if (type == value.getType()) {
                return value;
            }
        }
        return UNKNOWN;
    }

    public static boolean isFinalStatus(int orderStatus) {
        OrderStatusEnum statusEnum = ofType(orderStatus);
        return OrderStatusEnum.APPLY_SUCCESS.equals(statusEnum)
                || OrderStatusEnum.APPLY_FAIL.equals(statusEnum);
    }
}
