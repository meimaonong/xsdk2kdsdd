package com.art.app.common.pattern.entity;

import lombok.Data;

@Data
public class PaymentStatusParams {

    private String artOrderId;
    private Integer artOrderType;
    private String paymentOrderId;
    private Integer paymentStatus;
    private Integer paymentType;

}
