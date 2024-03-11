package com.art.app.web.bean.request.order;

import com.art.app.web.bean.request.BizParam;
import lombok.Data;

@Data
public class RefundApplyBizParam extends BizParam {

    private String orderId;
    private String refundReason;

}
