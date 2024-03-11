package com.art.app.web.bean.response;

import lombok.Data;

import java.util.Date;

@Data
public class VideoInfo {

    private int userId;
    private String avatar;
    private String author;
    private String title;
    private Date createdAt;
    private int viewNum;
    private int type;
    private String imgUrl;
    private String resourceId;
    private String videoUrl;
    private String detailUrl;
    private String description;
}
