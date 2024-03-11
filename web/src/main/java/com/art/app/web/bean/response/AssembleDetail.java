package com.art.app.web.bean.response;

import com.art.app.web.bean.AssembleMember;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AssembleDetail {

    private String title;
    private Date applyStopTime;
    private int assembleCount;
    private List<AssembleMember> contents;
}
