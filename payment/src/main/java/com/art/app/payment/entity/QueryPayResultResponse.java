package com.art.app.payment.entity;

import lombok.Data;

@Data
public class QueryPayResultResponse {

    private Integer result;
    private String remark;
    private String outOrderId;
}
