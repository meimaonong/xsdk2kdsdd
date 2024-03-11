package com.art.app.payment.entity.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;

public class CommonResponseParams {

    // SUCCESS/FAIL 此字段是通信标识，非交易标识
    @JsonProperty("return_code")
    private String returnCode;
    // 返回信息，如非空，为错误原因
    @JsonProperty("return_msg")
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
    // 签名
    private String sign;
    // SUCCESS/FAIL
    @JsonProperty("result_code")
    private String resultCode;
    // 错误码
    @JsonProperty("err_code")
    private String errCode;
    // 结果信息描述
    @JsonProperty("err_code_des")
    private String errCodeDes;

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

    @XmlElement(name = "sign")
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @XmlElement(name = "result_code")
    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    @XmlElement(name = "err_code")
    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    @XmlElement(name = "err_code_des")
    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }
}
