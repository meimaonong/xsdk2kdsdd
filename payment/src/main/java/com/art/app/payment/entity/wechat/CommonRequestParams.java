package com.art.app.payment.entity.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;

public class CommonRequestParams {

    // 微信开放平台审核通过的应用APPID
    @JsonProperty("appid")
    private String appId;
    // 微信支付分配的商户号
    @JsonProperty("mch_id")
    private String mchId;
    // 随机字符串，不长于32位。
    @JsonProperty("nonce_str")
    private String nonceStr;
    // 签名
    private String sign;

    @XmlElement(name = "appid")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @XmlElement(name = "mch_id")
    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    @XmlElement(name = "nonce_str")
    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    @XmlElement(name = "sign")
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
