package com.art.app.payment.entity.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
public class WechatQueryPayResultResponse extends CommonResponseParams {

    @JsonProperty("openid")
    private String openId;
    @JsonProperty("is_subscribe")
    private String isSubscribe;
    @JsonProperty("trade_type")
    private String tradeType;
    @JsonProperty("trade_state")
    private String tradeState;
    @JsonProperty("bank_type")
    private String bankType;
    @JsonProperty("total_fee")
    private Integer totalFee;
    @JsonProperty("cash_fee")
    private Integer cashFee;
    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    @JsonProperty("time_end")
    private String timeEnd;
    @JsonProperty("trade_state_desc")
    private String tradeStateDesc;
    @JsonProperty("fee_type")
    private String feeType;
    @JsonProperty("attach")
    private String attach;
    @JsonProperty("cash_fee_type")
    private String cashFeeType;

    @XmlElement(name = "openid")
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @XmlElement(name = "is_subscribe")
    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    @XmlElement(name = "trade_type")
    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    @XmlElement(name = "trade_state")
    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    @XmlElement(name = "bank_type")
    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
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

    @XmlElement(name = "time_end")
    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    @XmlElement(name = "trade_state_desc")
    public String getTradeStateDesc() {
        return tradeStateDesc;
    }

    public void setTradeStateDesc(String tradeStateDesc) {
        this.tradeStateDesc = tradeStateDesc;
    }

    @XmlElement(name = "fee_type")
    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    @XmlElement(name = "attach")
    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    @XmlElement(name = "cash_fee_type")
    public String getCashFeeType() {
        return cashFeeType;
    }

    public void setCashFeeType(String cashFeeType) {
        this.cashFeeType = cashFeeType;
    }
}
