package com.art.app.web.bean;

import lombok.Data;

@Data
public class ArticleDetail {

    private int type; // 0:图文，1：纯文本
    private String imgUrl;
    private String imgDesc;
    private String text;
    private int width;
    private int height;
}
