package com.art.test;

import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.art.app.common.Constants;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.common.util.OrderUtils;
import com.art.app.mw.redis.RedisUtil;
import com.art.app.payment.PaymentException;
import com.art.app.payment.client.config.AlipayConfig;
import com.art.app.payment.enums.PaymentErrorCodeEnum;
import com.art.app.payment.utils.JackJsonUtil;
import com.art.app.web.ArtApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ArtApplication.class}) // 指定启动类
public class RedisTest {

    @Resource
    private RedisUtil redisUtil;

    @Test
    public void testLock() {
        String key = Constants.ORDER_ASSEMBLE_PREFIX + 11111;
        String value = UUID.randomUUID().toString();
        boolean lock = false;
        try {
            lock = redisUtil.lock(key, value, 200, TimeUnit.SECONDS);
            if (lock) {
                Thread.sleep(100);
            } else {
                System.out.println("获取锁失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (lock) {
                boolean unLock = redisUtil.unLock(key, value);
                if (unLock) {
                    System.out.println("释放锁失败");
                }
            }
        }
    }


    public static void main(String[] args) {
        testAlipay();
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
