package com.art.app.web.bean.response;

import lombok.Data;

@Data
public class SearchListResult {

    private boolean hasMore = false;
    private Object contents;
}
