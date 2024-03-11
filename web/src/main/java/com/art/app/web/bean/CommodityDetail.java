package com.art.app.web.bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommodityDetail {

    private List<ProductResource> suggestProducts;
    private String content;
    private String title;
    private int userId;
    private String author;
    private String avatar;
    private Date createdAt;
    private int viewNum;
    private int likeNum;
    private String imgUrl;
    private String resourceId;
    private String description;
    private boolean like;
    private boolean focus;
}
