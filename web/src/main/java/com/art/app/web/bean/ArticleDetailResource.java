package com.art.app.web.bean;

import lombok.Data;

import java.util.List;

@Data
public class ArticleDetailResource extends ArticleResource {

    private int likeNum;
    private int from;
    private boolean like;
    private List<ArticleDetail> blocks;
}
