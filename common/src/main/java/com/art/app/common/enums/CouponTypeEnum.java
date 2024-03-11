package com.art.app.common.enums;

import lombok.Getter;

@Getter
public enum CouponTypeEnum {

    VOUCHER("抵用券", 0),
    ;

    private String desc;
    private int type;

    CouponTypeEnum(String desc, int type) {
        this.desc = desc;
        this.type = type;
    }

    public static CouponTypeEnum valueOfType(int type) {
        for (CouponTypeEnum value : CouponTypeEnum.values()) {
            if (value.getType() == type) {
                return value;
            }
        }
        return VOUCHER;
    }
}
