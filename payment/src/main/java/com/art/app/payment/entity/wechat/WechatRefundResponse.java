package com.art.app.payment.entity.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
public class WechatRefundResponse extends CommonResponseParams {

    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    @JsonProperty("out_refund_no")
    private String outRefundNo;
    @JsonProperty("refund_id")
    private String refundId;
    @JsonProperty("refund_fee")
    private Integer refundFee;
    @JsonProperty("settlement_refund_fee")
    private Integer settlementRefundFee;
    @JsonProperty("total_fee")
    private Integer totalFee;
    @JsonProperty("settlement_total_fee")
    private Integer settlementTotalFee;
    @JsonProperty("fee_type")
    private String feeType;
    @JsonProperty("cash_fee")
    private Integer cashFee;
    @JsonProperty("cash_fee_type")
    private String cashFeeType;
    @JsonProperty("cash_refund_fee")
    private Integer cashRefundFee;
//    @JsonProperty("coupon_type_$n")
//    private String coupon_type_$n;
    @JsonProperty("coupon_refund_fee")
    private Integer couponRefundFee;
//    @JsonProperty("coupon_refund_fee_$n")
//    private Integer coupon_refund_fee_$n;
    @JsonProperty("coupon_refund_count")
    private Integer couponRefundCount;
//    @JsonProperty("coupon_refund_id_$n")
//    private String coupon_refund_id_$n;

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

    @XmlElement(name = "out_refund_no")
    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    @XmlElement(name = "refund_id")
    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    @XmlElement(name = "refund_fee")
    public Integer getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Integer refundFee) {
        this.refundFee = refundFee;
    }

    @XmlElement(name = "settlement_refund_fee")
    public Integer getSettlementRefundFee() {
        return settlementRefundFee;
    }

    public void setSettlementRefundFee(Integer settlementRefundFee) {
        this.settlementRefundFee = settlementRefundFee;
    }

    @XmlElement(name = "total_fee")
    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    @XmlElement(name = "settlement_total_fee")
    public Integer getSettlementTotalFee() {
        return settlementTotalFee;
    }

    public void setSettlementTotalFee(Integer settlementTotalFee) {
        this.settlementTotalFee = settlementTotalFee;
    }

    @XmlElement(name = "fee_type")
    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    @XmlElement(name = "cash_fee")
    public Integer getCashFee() {
        return cashFee;
    }

    public void setCashFee(Integer cashFee) {
        this.cashFee = cashFee;
    }

    @XmlElement(name = "cash_fee_type")
    public String getCashFeeType() {
        return cashFeeType;
    }

    public void setCashFeeType(String cashFeeType) {
        this.cashFeeType = cashFeeType;
    }

    @XmlElement(name = "cash_refund_fee")
    public Integer getCashRefundFee() {
        return cashRefundFee;
    }

    public void setCashRefundFee(Integer cashRefundFee) {
        this.cashRefundFee = cashRefundFee;
    }

    @XmlElement(name = "coupon_refund_fee")
    public Integer getCouponRefundFee() {
        return couponRefundFee;
    }

    public void setCouponRefundFee(Integer couponRefundFee) {
        this.couponRefundFee = couponRefundFee;
    }

    @XmlElement(name = "coupon_refund_count")
    public Integer getCouponRefundCount() {
        return couponRefundCount;
    }

    public void setCouponRefundCount(Integer couponRefundCount) {
        this.couponRefundCount = couponRefundCount;
    }
}
