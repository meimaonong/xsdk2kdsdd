package com.art.app.common.enums;

import lombok.Getter;

@Getter
public enum SearchTypeEnum {
    //1:综合 2:视频 3:用户
    ARTICLE(1, "综合"),
    VIDEO(2, "视频"),
    USER(3, "用户"),
    ;

    private int type;
    private String name;

    SearchTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static SearchTypeEnum ofType(int type) {
        for (SearchTypeEnum value : SearchTypeEnum.values()) {
            if (type == value.getType()) {
                return value;
            }
        }
        return ARTICLE;
    }
}
