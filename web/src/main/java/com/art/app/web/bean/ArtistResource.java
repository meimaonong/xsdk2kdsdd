package com.art.app.web.bean;

import lombok.Data;

@Data
public class ArtistResource {

    private String letter;
    private String nickName;
    private String avatar;
    private int fanNum;
    private int userId;
    private boolean hasFocus = false;
}
