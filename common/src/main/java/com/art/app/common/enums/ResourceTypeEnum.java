package com.art.app.common.enums;

import lombok.Getter;

@Getter
public enum ResourceTypeEnum {
    ARTICLE(0, "文章"),
    PRODUCT(1, "精选"),
    TRAINING(2, "高研班"),
    COURSE(3, "教程"),
    ARTIST(4, "艺术家"),
    EXHIBITION(5, "艺术展览"),
    VIDEO_COURSE(6, "视频课程"),
    SKETCHING_ACTIVITY(7, "活动"),
    ;

    private int type;
    private String name;

    ResourceTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static ResourceTypeEnum ofType(int type) {
        for (ResourceTypeEnum value : ResourceTypeEnum.values()) {
            if (value.getType() == type) {
                return value;
            }
        }
        return ARTICLE;
    }
}
