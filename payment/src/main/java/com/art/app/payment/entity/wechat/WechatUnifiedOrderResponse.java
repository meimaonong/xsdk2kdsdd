package com.art.app.payment.entity.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 统一下单请求参数
 */
@XmlRootElement(name = "xml")
public class WechatUnifiedOrderResponse extends CommonResponseParams {

    // 商品描述交易字段格式根据不同的应用场景按照以下格式：
    // APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
    @JsonProperty("trade_type")
    private String tradeType;
    // 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。
    @JsonProperty("prepay_id")
    private String prepayId;

    @XmlElement(name = "trade_type")
    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    @XmlElement(name = "prepay_id")
    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

}
