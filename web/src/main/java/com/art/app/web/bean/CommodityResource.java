package com.art.app.web.bean;

import lombok.Data;

import java.util.Date;

@Data
public class CommodityResource {

    private int type;
    private String title;
    private int userId;
    private String author;
    private String avatar;
    private Date createdAt;
    private int viewNum;
    private String imgUrl;
    private String resourceId;

}
