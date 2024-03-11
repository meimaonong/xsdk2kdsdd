package com.art.app.payment.entity.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 统一下单请求参数
 */
@XmlRootElement(name = "xml")
public class WechatUnifiedOrderRequest extends CommonRequestParams {

    // 商品描述交易字段格式根据不同的应用场景按照以下格式：
    // APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
    private String body;
    // 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    // 订单总金额，单位为分
    @JsonProperty("total_fee")
    private Integer totalFee;
    // 调用微信支付API的机器IP
    @JsonProperty("spbill_create_ip")
    private String spbillCreateIp;
    // 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
    @JsonProperty("notify_url")
    private String notifyUrl;
    // 支付类型
    @JsonProperty("trade_type")
    private String tradeType;

    @JsonProperty("time_expire")
    private String timeExpire;

    @XmlElement(name = "body")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    @XmlElement(name = "spbill_create_ip")
    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    @XmlElement(name = "notify_url")
    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    @XmlElement(name = "trade_type")
    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    @XmlElement(name = "time_expire")
    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }
}
