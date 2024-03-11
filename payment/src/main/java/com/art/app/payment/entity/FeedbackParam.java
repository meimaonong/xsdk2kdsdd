package com.art.app.payment.entity;

import lombok.Data;

import java.util.Date;

@Data
public class FeedbackParam {

    private Integer result; // 1成功 2失败
    private String remark;
    private String paymentOrderId;
    private String outOrderId;
    private String responseStr;
    private Date successTime;
}
