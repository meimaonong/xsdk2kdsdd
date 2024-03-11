package com.art.app.web.bean.request;

import lombok.Data;

import java.util.Map;

@Data
public class RequestVo {

    private String signature;
    private BaseParams baseParams;
    private Map<String, Object> bizParams;

    @Data
    public static class BaseParams {
        private String clientId;
        private String token;
        private String appVersion;
        private int source;
        private long timestamp;
        private Map<String, Object> extras;
    }

}
