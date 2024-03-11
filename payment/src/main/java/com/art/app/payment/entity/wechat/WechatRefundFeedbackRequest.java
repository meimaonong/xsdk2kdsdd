package com.art.app.payment.entity.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
public class WechatRefundFeedbackRequest {

    // SUCCESS/FAIL 此字段是通信标识，非交易标识
    private String returnCode;
    // 返回信息，如非空，为错误原因
    private String returnMsg;
    // 微信开放平台审核通过的应用APPID
    @JsonProperty("appid")
    private String appId;
    // 微信支付分配的商户号
    @JsonProperty("mch_id")
    private String mchId;
    // 随机字符串，不长于32位。
    @JsonProperty("nonce_str")
    private String nonceStr;
    // 加密信息
    @JsonProperty("req_info")
    private String reqInfo;

    @XmlElement(name = "return_code")
    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    @XmlElement(name = "return_msg")
    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

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

    @XmlElement(name = "req_info")
    public String getReqInfo() {
        return reqInfo;
    }

    public void setReqInfo(String reqInfo) {
        this.reqInfo = reqInfo;
    }
}
