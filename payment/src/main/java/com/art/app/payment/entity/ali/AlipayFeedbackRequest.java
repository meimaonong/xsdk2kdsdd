package com.art.app.payment.entity.ali;

import com.art.app.common.serializer.DateTimeDeserializer;
import com.art.app.common.serializer.DateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AlipayFeedbackRequest {

    @JsonProperty("trade_no")
    private String tradeNo;
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    @JsonProperty("out_biz_no")
    private String outBizNo;
    @JsonProperty("trade_status")
    private String tradeStatus;
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;
    @JsonProperty("receipt_amount")
    private BigDecimal receiptAmount;
    @JsonProperty("refund_fee")
    private BigDecimal refundFee;
    @JsonProperty("gmt_create")
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private Date gmtCreate;
    @JsonProperty("gmt_payment")
    private Date gmtPayment;
    @JsonProperty("gmt_refund")
    private Date gmtRefund;
    @JsonProperty("gmt_close")
    private Date gmtClose;
}
