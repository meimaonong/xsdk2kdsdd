package com.art.app.common.enums;

import lombok.Getter;

@Getter
public enum MessageTypeEnum {
    SYSTEM(1, "系统消息"),
    ACTIVITY(2, "活动消息"),
    ;

    private int type;
    private String name;

    MessageTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static MessageTypeEnum ofType(int type) {
        for (MessageTypeEnum value : MessageTypeEnum.values()) {
            if (type == value.getType()) {
                return value;
            }
        }
        return null;
    }
}
