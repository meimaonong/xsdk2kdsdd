package com.art.app.common.enums;

import lombok.Getter;

@Getter
public enum VerifyCodeEnum {

    REGISTER(0, "注册"),
    LOGIN(1, "登录"),
    PUBLISH(2, "发表文章"),
    ;

    private int type;
    private String name;

    VerifyCodeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
