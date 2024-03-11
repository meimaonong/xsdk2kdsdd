package com.art.app.web.bean.request.activity;

import com.art.app.web.bean.request.BizParam;
import lombok.Data;

import java.util.List;

@Data
public class ActivitySubListBizParam extends BizParam {

    private Integer type; //2:高研班，3：艺术展览，4:活动
    private Integer pageIndex;
    private Integer pageSize;

}
