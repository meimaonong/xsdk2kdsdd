package com.art.app.common.enums;

import lombok.Getter;

@Getter
public enum MemberActionTypeEnum {
    READ(0, 5, "浏览文章"),
    INVITE(1, 10, "成功邀请"),
    ;

    private int type;
    private int points;
    private String desc;

    MemberActionTypeEnum(int type, int points, String desc) {
        this.type = type;
        this.points = points;
        this.desc = desc;
    }

    public static MemberActionTypeEnum ofType(int type) {
        for (MemberActionTypeEnum value : MemberActionTypeEnum.values()) {
            if (type == value.getType()) {
                return value;
            }
        }
        return READ;
    }
}
