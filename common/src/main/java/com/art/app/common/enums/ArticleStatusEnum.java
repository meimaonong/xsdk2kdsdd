package com.art.app.common.enums;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

@Getter
public enum ArticleStatusEnum {
    //0:未审核 1:审核中  2:打回 3: 已审核
    DEFAULT(0, "待审核", "审核中"),
    AUDITING(1, "审核中", "审核中"),
    OFFLINE(2, "下架", "审核未通过"),
    ONLINE(3, "在架", "审核通过"),
    ;

    private int status;
    private String description;
    private String displayName;

    ArticleStatusEnum(int status, String description, String displayName) {
        this.status = status;
        this.description = description;
        this.displayName = displayName;
    }

    public static List<Integer> personalViewStatus =
            Lists.newArrayList(DEFAULT.getStatus(), AUDITING.getStatus(), OFFLINE.getStatus(),
                    ONLINE.getStatus());

    public static ArticleStatusEnum ofStatus(int status) {
        for (ArticleStatusEnum value : ArticleStatusEnum.values()) {
            if (status == value.getStatus()) {
                return value;
            }
        }
        return DEFAULT;
    }
}
