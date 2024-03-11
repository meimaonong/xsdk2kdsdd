package com.art.app.web.bean.response.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExhibitionOrderResult {

    private String orderId;
    private BigDecimal amount;

}
