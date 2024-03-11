package com.art.app.web.bean.response;

import com.art.app.common.enums.ContentTypeEnum;
import lombok.Data;

@Data
public class HomePageConfigResult {

    private int type;
    private String name;

    public HomePageConfigResult(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public HomePageConfigResult(ContentTypeEnum typeEnum) {
        this.type = typeEnum.getType();
        this.name = typeEnum.getName();
    }

    public HomePageConfigResult() {
    }
}
