package com.art.app.payment.entity.ali;

import com.art.app.payment.entity.PrePayResponseParams;
import lombok.Data;

@Data
public class AlipayPrePayResponseParams extends PrePayResponseParams {

    private String orderString;
}
