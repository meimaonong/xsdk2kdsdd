package com.art.app.web.bean;

import lombok.Data;

@Data
public class CarouselResource {

    private String title;
    private String author;
    private String resourceId;
    private int type;
    private String imgUrl;
    private String detailUrl;
}
