package com.art.app.payment.entity.wechat;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
public class WechatPayFeedbackResponse {

    // SUCCESS/FAIL 此字段是通信标识，非交易标识
    private String returnCode;
    // 返回信息，如非空，为错误原因
    private String returnMsg;

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

}
