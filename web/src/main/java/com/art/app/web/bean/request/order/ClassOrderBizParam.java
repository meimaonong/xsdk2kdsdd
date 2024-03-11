package com.art.app.web.bean.request.order;

import com.art.app.web.bean.request.BizParam;
import lombok.Data;

import java.util.List;

@Data
public class ClassOrderBizParam extends BizParam {

    private String resourceId; // 高研班id
    private Integer assembleId; // 拼团id
    private String name; // 报名人姓名
    private String phone; // 报名人手机号码
    private String remark; // 备注信息
    private List<String> works; // 作品列表

}
