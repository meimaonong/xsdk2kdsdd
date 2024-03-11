package com.art.app.web.bean;

import lombok.Data;

@Data
public class PushToken {

    private int channel; // 0:极光
    private String deviceToken;
}
