package com.art.app.payment.entity;

import lombok.Data;

@Data
public class PaymentTypeInfo {

    // 支付类型 1-支付宝 2-微信
    private Integer paymentType;
    // 支付类型名称
    private String paymentTypeName;
    // 1-打开 2-关闭
    private Integer status;
    // 备注信息
    private String remark;
}
