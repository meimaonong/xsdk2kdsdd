package com.art.app.web.bean.response;

import lombok.Data;

@Data
public class PersonalBriefResult {
    private String nickName;
    private String degree;
    private int level;
    private String avatar;
    private String introduction;
    private String bgImgUrl;
    private String motto;
    private int fanNum;
    private int focusNum;
    private int viewNum;
    private int draftNum;
    private int points;
    private int couponUnUsedNum;
    private int msgUnReadNum;
    private boolean focus = false;
    private boolean artist = false;
}
