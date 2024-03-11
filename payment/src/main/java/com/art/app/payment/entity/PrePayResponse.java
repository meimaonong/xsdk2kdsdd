package com.art.app.payment.entity;

import lombok.Data;

@Data
public class PrePayResponse<T extends PrePayResponseParams> {

    private Integer paymentType;
    private T params;
}
