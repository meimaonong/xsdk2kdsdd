package com.art.app.web.bean.response;

import com.art.app.web.bean.ArticleResource;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RecommendResult extends HomePageResult {

    private Map<String, Object> ad;

    private List<ArticleResource> contents = Lists.newArrayList();
}
