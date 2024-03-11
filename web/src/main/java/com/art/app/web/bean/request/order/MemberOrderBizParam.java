package com.art.app.web.bean.request.order;

import com.art.app.web.bean.request.BizParam;
import lombok.Data;

@Data
public class MemberOrderBizParam extends BizParam {

    private Integer memberLevel; // 会员等级
    private String remark;

}
