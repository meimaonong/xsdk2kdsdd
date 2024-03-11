package com.art.app.common.enums;

import lombok.Getter;

@Getter
public enum CouponStatusEnum {

    NOT_USED("有效期内未使用", 0),
    USED("有效期内已使用", 1),
    EXPIRED("已过期", 2),
    ;

    private String desc;
    private int type;

    CouponStatusEnum(String desc, int type) {
        this.desc = desc;
        this.type = type;
    }

    public static CouponStatusEnum valueOfType(int type) {
        for (CouponStatusEnum value : CouponStatusEnum.values()) {
            if (value.getType() == type) {
                return value;
            }
        }
        return EXPIRED;
    }
}
