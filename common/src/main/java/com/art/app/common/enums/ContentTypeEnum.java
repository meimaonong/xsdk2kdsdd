package com.art.app.common.enums;

import lombok.Getter;

@Getter
public enum ContentTypeEnum {
    CAROUSEL(0, 0, 0, "轮播"),
    RECOMMEND(1, 0, 0, "推荐"),
    VIDEO(10, 0, 0, "视频"),
    WORK(20, 0, 0, "作品"),
    ACTIVITY(30, 0, 0, "活动"),
    ARTIST(40, 1, 4, "艺术家"),
    FOCUS(50, 0, 0, "关注"),
    BOOK(60, 0, 1, "书籍"),
    PAINT(70, 1, 1, "画材"),
    BASE(90, 0, 0, "基地"),
    TRAINING(100, 0, 2, "高研班"),
    COURSE(110, 0, 3, "课程"),
    SEARCH(120, 0, 0, "搜索热词"),

    EXHIBITION(130, 0, 0, "艺术展览"),
    VIDEO_COURSE(140, 0, 0, "视频课程"),
    SKETCHING(150, 0, 0, "活动"),

    ;

    private int type;
    private int resourceType;
    private int category;
    private String name;

    ContentTypeEnum(int type, int resourceType, int category, String name) {
        this.type = type;
        this.resourceType = resourceType;
        this.category = category;
        this.name = name;
    }

    public static ContentTypeEnum ofType(int type) {
        for (ContentTypeEnum value : ContentTypeEnum.values()) {
            if (type == value.getType()) {
                return value;
            }
        }
        return RECOMMEND;
    }
}
