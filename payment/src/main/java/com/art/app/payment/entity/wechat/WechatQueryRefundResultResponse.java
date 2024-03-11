package com.art.app.payment.entity.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
public class WechatQueryRefundResultResponse extends CommonResponseParams {

    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    @JsonProperty("total_fee")
    private Integer totalFee;
    @JsonProperty("cash_fee")
    private Integer cashFee;
    @JsonProperty("refund_count")
    private Integer refundCount;
    @JsonProperty("out_refund_no_0")
    private String outRefundNo0;
    @JsonProperty("refund_id_0")
    private String refundId0;
    @JsonProperty("refund_fee_0")
    private Integer refundFee0;
    @JsonProperty("refund_status_0")
    private String refundStatus0;
    @JsonProperty("refund_recv_accout_0")
    private String refundRecvAccout0;

    @XmlElement(name = "transaction_id")
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @XmlElement(name = "out_trade_no")
    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    @XmlElement(name = "total_fee")
    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    @XmlElement(name = "cash_fee")
    public Integer getCashFee() {
        return cashFee;
    }

    public void setCashFee(Integer cashFee) {
        this.cashFee = cashFee;
    }

    @XmlElement(name = "refund_count")
    public Integer getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(Integer refundCount) {
        this.refundCount = refundCount;
    }

    @XmlElement(name = "out_refund_no_0")
    public String getOutRefundNo0() {
        return outRefundNo0;
    }

    public void setOutRefundNo0(String outRefundNo0) {
        this.outRefundNo0 = outRefundNo0;
    }

    @XmlElement(name = "refund_id_0")
    public String getRefundId0() {
        return refundId0;
    }

    public void setRefundId0(String refundId0) {
        this.refundId0 = refundId0;
    }

    @XmlElement(name = "refund_fee_0")
    public Integer getRefundFee0() {
        return refundFee0;
    }

    public void setRefundFee0(Integer refundFee0) {
        this.refundFee0 = refundFee0;
    }

    @XmlElement(name = "refund_status_0")
    public String getRefundStatus0() {
        return refundStatus0;
    }

    public void setRefundStatus0(String refundStatus0) {
        this.refundStatus0 = refundStatus0;
    }

    @XmlElement(name = "refund_recv_accout_0")
    public String getRefundRecvAccout0() {
        return refundRecvAccout0;
    }

    public void setRefundRecvAccout0(String refundRecvAccout0) {
        this.refundRecvAccout0 = refundRecvAccout0;
    }
}
