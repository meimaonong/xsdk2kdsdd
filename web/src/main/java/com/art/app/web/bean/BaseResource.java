package com.art.app.web.bean;

import lombok.Data;

import java.util.Date;

@Data
public class BaseResource {

    private int type;
    private int userId;
    private String title;
    private String author;
    private String avatar;
    private Date createdAt;
    private int viewNum;
}
