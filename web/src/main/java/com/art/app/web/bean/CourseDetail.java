package com.art.app.web.bean;

import lombok.Data;

import java.util.Date;

@Data
public class CourseDetail {

    private String title;
    private Date createdAt;
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
}
