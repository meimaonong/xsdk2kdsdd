package com.art.app.payment.entity;

import lombok.Data;

@Data
public class RefundRequest {

    private String orderId;
    private String outOrderId;
    private String refundReason;
}
