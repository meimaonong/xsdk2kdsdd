package com.art.app.web.bean;

import lombok.Data;

import java.util.Date;

@Data
public class ExhibitionDetail {

    private String title;
    private int status;
    private Date createdAt;
    private Date applyStopTime;
    private Date classStopTime;
    private Date classStartTime;
    private int isSignUp;
    private int isGroup;
    private int assembleTotalCount;
    private int joinAssemble;
    private int purchaseStatus;
    private long assembleGroupId;
    private int assembleCount;
    private String rule;
    private int viewNum;
    private String imgUrl;
    private String resourceId;
    private String url;
    private String content;
    private int userId;
    private String author;
    private String avatar;
    private boolean like;
    private int likeNum;
    private String description;
    private UserBasicInfo sponsorInfo;
}
