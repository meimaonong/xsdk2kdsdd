package com.art.app.common.enums;

import lombok.Getter;

@Getter
public enum ArticleTagEnum {
    VIDEO(1, "视频"),
    WORK(2, "作品"),
    ACTIVITY(3, "活动"),
    LIVE_BASE(4, "基地"),
    ;

    private int type;
    private String name;

    ArticleTagEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static ArticleTagEnum ofType(int type) {
        for (ArticleTagEnum value : ArticleTagEnum.values()) {
            if (type == value.getType()) {
                return value;
            }
        }
        return null;
    }

    public static ArticleTagEnum fromContentTypeEnum(ContentTypeEnum contentTypeEnum) {
        switch (contentTypeEnum) {
            case VIDEO:
                return VIDEO;
            case WORK:
                return WORK;
            case ACTIVITY:
                return ACTIVITY;
            default:
                break;
        }
        return null;
    }

}
