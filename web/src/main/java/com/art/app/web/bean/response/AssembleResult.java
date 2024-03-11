package com.art.app.web.bean.response;

import com.art.app.web.bean.AssembleBasicInfo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AssembleResult {

    private boolean hasMore = false;
    private String title;
    private Date applyStopTime;
    private List<AssembleBasicInfo> contents;
}
