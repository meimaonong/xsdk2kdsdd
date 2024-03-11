package com.art.app.web.bean.response;

import com.art.app.web.bean.BanAuthorResource;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class BanAuthorResult {

    private boolean hasMore = false;
    private List<BanAuthorResource> contents = Lists.newArrayList();
}
