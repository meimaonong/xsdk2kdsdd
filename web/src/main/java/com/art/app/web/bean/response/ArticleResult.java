package com.art.app.web.bean.response;

import com.art.app.web.bean.ArticleResource;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class ArticleResult extends ArticleResource {

    private int likeNum = 0;
    private int from;
    private boolean like = false;
    private boolean focus = false;
    private Object content;
    private List<ArticleResource> suggestions = Lists.newArrayList();
}
