package com.art.app.web.bean.request.order;

import com.art.app.payment.utils.JackJsonUtil;
import com.art.app.web.bean.request.BizParam;
import com.art.app.web.bean.request.RequestVo;
import lombok.Data;

@Data
public class OrderRequestVo<T extends BizParam> extends RequestVo {

    public T getOrderBizParams(Class<T> cls) {
        return JackJsonUtil.toBean(JackJsonUtil.toJson(getBizParams()), cls);
    }

}
