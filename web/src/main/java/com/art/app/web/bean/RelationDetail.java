package com.art.app.web.bean;

import lombok.Data;

@Data
public class RelationDetail {
    private int userId;
    private String nickName;
    private String avatar;
    private int viewNum = 0;
    private boolean hasFocus = false;
}
