package com.art.app.common.enums;

import lombok.Getter;

@Getter
public enum RelationTypeEnum {
    ALL(0, "all"),
    FOCUS(1, "关注"),
    FAN(2, "粉丝"),
    ;

    private int type;
    private String name;

    RelationTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static RelationTypeEnum ofType(int type) {
        for (RelationTypeEnum value : RelationTypeEnum.values()) {
            if (type == value.getType()) {
                return value;
            }
        }
        return ALL;
    }
}
