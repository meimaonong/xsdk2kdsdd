package com.art.app.common.enums;

import com.art.app.common.util.DatetimeUtils;

import java.time.LocalDateTime;

public enum OssStorageTypeEnum {
    DEFAULT(0, "xsl/default/", "默认"),
    AVATAR(1, "xsl/avatar/", "用户头像"),
    IMAGE(2, "xsl/image/", "文章的图片"),
    VIDEO(3, "xsl/video/", "文章的视频"),
    ;

    public int getType() {
        return type;
    }

    public String getDir() {
        LocalDateTime localDateTime = DatetimeUtils.getNow();
        return dir + localDateTime.getYear() + "/" +
                localDateTime.getMonthValue() + "/" +
                localDateTime.getDayOfMonth() + "/";
    }

    public String getDescription() {
        return description;
    }

    private int type;
    private String dir;
    private String description;

    OssStorageTypeEnum(int type, String dir, String description) {
        this.type = type;
        this.dir = dir;
        this.description = description;
    }

    public static OssStorageTypeEnum ofType(int type) {
        for (OssStorageTypeEnum value : OssStorageTypeEnum.values()) {
            if (value.getType() == type) {
                return value;
            }
        }
        return DEFAULT;
    }
}
