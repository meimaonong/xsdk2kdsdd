package com.art.app.web.bean.response;

import com.art.app.web.bean.RelationDetail;
import lombok.Data;

import java.util.List;

@Data
public class RelationResult {

    private boolean hasMore = false;
    private int type;
    private String name;
    private List<RelationDetail> contents;
}
