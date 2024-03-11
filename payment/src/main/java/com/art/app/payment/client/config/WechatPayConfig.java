package com.art.app.payment.client.config;

public class WechatPayConfig {

    // (安全考虑，此处的appId、mchId、apiKey都是假数据，需要真实数据替换)
    private final static String APP_ID = "";
    private final static String MCH_ID = "";
    private final static String API_KEY = "";

    public static String getWechatAppId() {
        return APP_ID;
    }

    public static String getWechatMchId() {
        return MCH_ID;
    }

    public static String getWechatApiKey() {
        return API_KEY;
    }
}
