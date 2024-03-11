package com.art.app.web.bean.response;

import com.art.app.web.bean.WorkResource;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class WorkResult extends HomePageResult {

    private List<WorkResource> workResources = Lists.newArrayList();
}
