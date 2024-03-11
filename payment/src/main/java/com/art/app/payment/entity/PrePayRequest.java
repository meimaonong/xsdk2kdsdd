package com.art.app.payment.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PrePayRequest {

    private String orderId;
    private Integer orderType;
    private Integer paymentType;
    private BigDecimal amount;
    private String subject;
    // 订单超时时间
    private Date timeExpire;
}
