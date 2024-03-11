package com.art.app.payment.client.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.art.app.payment.client.AlipayCustomerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;

@Slf4j
public class AlipayConfig implements InitializingBean {

    // 只有一套环境，配置写死在代码里 (已清空方便学习)
    private final static String APP_ID = "";
    private final static String APP_PRIVATE_KEY = "";
    private final static String APP_PUBLIC_KEY = "";
    private final static String ALIPAY_PUBLIC_KEY = "";

    private final static String URL = "https://openapi.alipay.com/gateway.do";
    private final static String FORMAT = "json";
    private final static String CHARSET = "UTF-8";
    private final static String SIGN_TYPE = "RSA2";

    private volatile static AlipayClient alipayClient;

    public static AlipayClient getAlipayClientInstance() {
        if (null == alipayClient) {
            synchronized (AlipayConfig.class) {
                if (null == alipayClient) {
                    alipayClient = initAlipayClient();
                }
            }
        }
        return alipayClient;
    }

    public static boolean verifySign(Map<String, String> params) {
        boolean result = false;
        try {
            result = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET,SIGN_TYPE);
        } catch (AlipayApiException e) {
            log.error(e.getErrMsg(), e);
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (null == alipayClient) {
            alipayClient = initAlipayClient();
        }
    }

    private static AlipayClient initAlipayClient() {
        return new AlipayCustomerClient(URL, APP_ID, APP_PRIVATE_KEY,
                FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
    }
}
