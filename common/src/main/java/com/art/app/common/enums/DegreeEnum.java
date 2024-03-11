package com.art.app.common.enums;

import lombok.Getter;

@Getter
public enum DegreeEnum {
    NORMAL(0, "普通用户"),
    MEMBER(1, "普通会员"),
    GOLDEN(2, "高级会员"),
    ;

    private int type;
    private String name;

    DegreeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static DegreeEnum ofType(int type) {
        for (DegreeEnum value : DegreeEnum.values()) {
            if (type == value.getType()) {
                return value;
            }
        }
        return NORMAL;
    }
}
