package com.art.app.common.pattern.entity;

import lombok.Data;

@Data
public class RefundStatusParams {

    private String artOrderId;
    private Integer artOrderType;
    private String paymentOrderId;
    private String refundOrderId;
    private Integer refundStatus;

}
