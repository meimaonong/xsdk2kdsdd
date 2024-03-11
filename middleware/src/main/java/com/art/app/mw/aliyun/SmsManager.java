package com.art.app.mw.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SmsManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsManager.class);

    @Autowired
    private IAcsClient iAcsClient;
    @Autowired
    private AliyunConfig aliyunConfig;

    public void sendSms(String phone, String code) throws ClientException {
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(AliyunConfig.sign);
        request.setTemplateCode(aliyunConfig.getTemplate());
        Map<String, String> params = Maps.newHashMapWithExpectedSize(1);
        params.put("code", code);
        request.setTemplateParam(JSON.toJSONString(params));
        iAcsClient.getAcsResponse(request);
    }
}
