package com.art.app.payment.entity;

import lombok.Data;

@Data
public class QueryRefundResultRequest {

    private String orderId;
    private String outOrderId;
}
