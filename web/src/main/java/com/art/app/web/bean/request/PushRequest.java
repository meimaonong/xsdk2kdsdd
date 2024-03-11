package com.art.app.web.bean.request;

import lombok.Data;

import java.util.List;

@Data
public class PushRequest {
    /**
     * target : ["18171adc03843f1a020"]
     * platform : 2
     * msgIds : [13]
     */

    private int platform;
    private List<String> target;
    private List<Long> msgIds;

}
