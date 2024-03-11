package com.art.app.web.bean.response.activity;

import lombok.Data;

@Data
public class ActivityList {

    private Integer type; //2:高研班，5：艺术展览，:活动
    private String title;
    private String detail;

}
