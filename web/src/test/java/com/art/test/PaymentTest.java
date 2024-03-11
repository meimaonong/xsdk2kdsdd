package com.art.test;

import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.common.util.OrderUtils;
import com.art.app.common.util.XmlUtils;
import com.art.app.payment.PaymentException;
import com.art.app.payment.client.config.AlipayConfig;
import com.art.app.payment.entity.wechat.WechatPayFeedbackRequest;
import com.art.app.payment.enums.PaymentErrorCodeEnum;
import com.art.app.payment.utils.JackJsonUtil;

import java.util.HashMap;
import java.util.Map;

public class PaymentTest {


    public static void main(String[] args) {
//        testAlipay();
        testXml();
    }

    public static void testXml() {
        String xml = "<xml><appid><![CDATA[wx8e22176dc162a64b]]></appid><bank_type><![CDATA[OTHERS]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1493825602]]></mch_id><nonce_str><![CDATA[a96c186eb1744ec08a7d556a8e0e950a]]></nonce_str><openid><![CDATA[osh8B1E3OinPpBspVW2JzOC1XeYk]]></openid><out_trade_no><![CDATA[PAY202006101705230367982102]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[CF242308A5F9CA539359E46B3733FEC3]]></sign><time_end><![CDATA[20200610170528]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[APP]]></trade_type><transaction_id><![CDATA[4200000617202006100231024380]]></transaction_id></xml>";
        WechatPayFeedbackRequest wechatPayFeedbackRequest = XmlUtils.xml2Object(xml, WechatPayFeedbackRequest.class);
        System.out.println(JackJsonUtil.toJson(wechatPayFeedbackRequest));

        Map<String, Object> map = XmlUtils.xml2Object(xml, Map.class);
        System.out.println(JackJsonUtil.toJson(map));
    }

    public static void testAlipay() {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
//        model.setSubject("测试");
//        model.setOutTradeNo(createOrderId("PAY"));
//        model.setTotalAmount("0.01");
//        model.setTimeExpire(DatetimeUtils.formatDatetime(DatetimeUtils.parseDatetime("2020-05-13 16:10:22"), DatetimeUtils.YYYY_MM_DD_HH_MM));
//        request.setBizModel(model);
        Map<String, Object> map = new HashMap<>();
        map.put("subject", "测试");
        map.put("out_trade_no", OrderUtils.createOrderId("PAY"));
        map.put("total_amount", "0.01");
        map.put("time_expire", DatetimeUtils.formatDatetime(DatetimeUtils.parseDatetime("2020-05-13 16:10:22"), DatetimeUtils.YYYY_MM_DD_HH_MM));
        request.setBizContent(JackJsonUtil.toJson(map));
        request.setNotifyUrl("http://58.212.96.49:8080/feedback/alipay/notify");
        System.out.println(JackJsonUtil.toJson(request));
        AlipayTradeAppPayResponse alipayTradeAppPayResponse = null;
        try {
            alipayTradeAppPayResponse = AlipayConfig.getAlipayClientInstance().sdkExecute(request);
        } catch (AlipayApiException e) {
            throw new PaymentException(PaymentErrorCodeEnum.ALIPAY_PRE_PAY_REQUEST_ERROR, e);
        }
        System.out.println(JackJsonUtil.toJson(alipayTradeAppPayResponse));
    }

}
