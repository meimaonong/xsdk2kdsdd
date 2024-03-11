package com.art.app.web.bean.response.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClassOrderResult {

    private String orderId;
    private BigDecimal amount;

}
