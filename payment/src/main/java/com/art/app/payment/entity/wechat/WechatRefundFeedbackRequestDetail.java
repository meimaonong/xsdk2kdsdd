package com.art.app.payment.entity.wechat;

import com.art.app.common.serializer.DateTimeDeserializer;
import com.art.app.common.serializer.DateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "root")
public class WechatRefundFeedbackRequestDetail {

    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    @JsonProperty("refund_id")
    private String refundId;
    @JsonProperty("out_refund_no")
    private String outRefundNo;
    @JsonProperty("total_fee")
    private Integer totalFee;
    @JsonProperty("refund_fee")
    private Integer refundFee;
    @JsonProperty("settlement_refund_fee")
    private Integer settlementRefundFee;
    @JsonProperty("refund_status")
    private String refundStatus;
    @JsonProperty("success_time")
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private Date successTime;
    @JsonProperty("refund_recv_accout")
    private String refundRecvAccout;
    @JsonProperty("refund_account")
    private String refundAccount;
    @JsonProperty("refund_request_source")
    private String refundRequestSource;

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

    @XmlElement(name = "refund_id")
    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    @XmlElement(name = "out_refund_no")
    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    @XmlElement(name = "total_fee")
    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
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

    @XmlElement(name = "refund_status")
    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    @XmlElement(name = "success_time")
    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    @XmlElement(name = "refund_recv_accout")
    public String getRefundRecvAccout() {
        return refundRecvAccout;
    }

    public void setRefundRecvAccout(String refundRecvAccout) {
        this.refundRecvAccout = refundRecvAccout;
    }

    @XmlElement(name = "refund_account")
    public String getRefundAccount() {
        return refundAccount;
    }

    public void setRefundAccount(String refundAccount) {
        this.refundAccount = refundAccount;
    }

    @XmlElement(name = "refund_request_source")
    public String getRefundRequestSource() {
        return refundRequestSource;
    }

    public void setRefundRequestSource(String refundRequestSource) {
        this.refundRequestSource = refundRequestSource;
    }
}
