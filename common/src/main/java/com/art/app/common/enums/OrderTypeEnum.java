package com.art.app.common.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum OrderTypeEnum {
    UNKNOWN(0, "未知"),
    MEMBER(1, "购买会员"),
    CLASS(2, "高研班"),
    EXHIBITION(3, "艺术展览"),
    SKETCHING(4, "活动"),
    ;

    private int type;
    private String name;
    private boolean isActivity;

    OrderTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static OrderTypeEnum ofType(int type) {
        for (OrderTypeEnum value : OrderTypeEnum.values()) {
            if (type == value.getType()) {
                return value;
            }
        }
        return UNKNOWN;
    }

    public static List<OrderTypeEnum> getActivityList() {
        List<OrderTypeEnum> results = new ArrayList<>();
        results.add(OrderTypeEnum.CLASS);
        results.add(OrderTypeEnum.SKETCHING);
        results.add(OrderTypeEnum.EXHIBITION);
        return results;
    }
}
