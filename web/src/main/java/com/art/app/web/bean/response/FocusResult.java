package com.art.app.web.bean.response;

import com.art.app.web.bean.ArticleResource;
import com.art.app.web.bean.ArtistResource;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class FocusResult extends HomePageResult {

    private List<ArticleResource> articles = Lists.newArrayList();

    private List<ArtistResource> artist = Lists.newArrayList();
}
