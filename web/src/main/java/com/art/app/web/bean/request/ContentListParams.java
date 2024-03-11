package com.art.app.web.bean.request;

import lombok.Data;

@Data
public class ContentListParams {

    private int pageSize;
    private int pageIndex;
    private int type;
}
