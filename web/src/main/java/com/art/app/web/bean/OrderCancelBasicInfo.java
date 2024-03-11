package com.art.app.web.bean;

import lombok.Data;

import java.util.Date;

@Data
public class OrderCancelBasicInfo {

    private Integer id;
    private String orderId;
    private Integer orderType;
    private Integer orderStatus;
    private Integer paymentStatus;
    private String subject;
    private Date createdAt;
    private Date timeExpire;
}
