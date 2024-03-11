package com.art.app.payment.entity.wechat;

import com.art.app.payment.entity.PrePayResponseParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WechatPrePayResponseParams extends PrePayResponseParams {

    @JsonProperty("appid")
    private String appId;
    @JsonProperty("partnerid")
    private String partnerId;
    @JsonProperty("prepayid")
    private String prePayId;
    @JsonProperty("package")
    private String wechatPackage;
    @JsonProperty("noncestr")
    private String nonceStr;
    @JsonProperty("timestamp")
    private String timestamp;
    private String sign;
}
