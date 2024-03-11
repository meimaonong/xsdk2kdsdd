package com.art.test;

import com.art.app.common.enums.PaymentStatusEnum;
import com.art.app.common.enums.PaymentTypeEnum;
import com.art.app.common.pattern.IPaymentStatusSubject;
import com.art.app.common.pattern.entity.PaymentStatusParams;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.common.util.HttpUtil;
import com.art.app.payment.entity.PrePayRequest;
import com.art.app.payment.entity.PrePayResponse;
import com.art.app.payment.enums.WechatPayUrlEnum;
import com.art.app.payment.service.IPaymentService;
import com.art.app.payment.utils.JackJsonUtil;
import com.art.app.web.ArtApplication;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ArtApplication.class}) // 指定启动类
public class ArtApplicationTest {

    @Resource
    private IPaymentService iPaymentService;
    @Resource
    private IPaymentStatusSubject iPaymentStatusSubject;

    @Test
    public void testOne() {
        System.out.println("test hello 1");
    }

    @Test
    public void testTwo() {
        System.out.println("test hello 2");
        TestCase.assertEquals(1, 1);
    }

    @Test
    public void testPaymentStatusSubject() {
        PaymentStatusParams params = new PaymentStatusParams();
        params.setArtOrderId("MEM202006021207095699718709");
        params.setArtOrderType(2);
        params.setPaymentStatus(PaymentStatusEnum.PAY_SUCCESS.getStatus());
        params.setPaymentType(2);
        iPaymentStatusSubject.accept(params);
    }

    @Test
    public void testWechatPrePay() {
        PrePayRequest prePayRequest = new PrePayRequest();
        prePayRequest.setOrderId("u7jh69hyrejkauiot");
        prePayRequest.setOrderType(2);
        prePayRequest.setAmount(BigDecimal.valueOf(0.01));
        prePayRequest.setPaymentType(PaymentTypeEnum.WECHAT.getType());
        prePayRequest.setSubject("白纸");
        prePayRequest.setTimeExpire(DatetimeUtils.parseDatetime("2020-05-27 23:30:00"));
        PrePayResponse prePayResponse = iPaymentService.prePay(prePayRequest);
        System.out.println(JackJsonUtil.toJson(prePayResponse));
    }

    @Before
    public void testBefore() {
        System.out.println("before");
    }

    @After
    public void testAfter() {
        System.out.println("after");
    }

    public String postSSL(WechatPayUrlEnum urlEnum) throws Exception {
        String reqJsonStr = "<xml>\n" +
                "    <appid>wx8e22176dc162a64b</appid>\n" +
                "    <mch_id>1493825602</mch_id>\n" +
                "    <nonce_str>ea08ca6564ae4c039fa90d58e7617d03</nonce_str>\n" +
                "    <sign>B88BB1DE7C507E9A977A9FAA700BA1A2</sign>\n" +
                "    <body>paper white</body>\n" +
                "    <notify_url>http://xsl.meimaonong.com/feedback/wechat/notify</notify_url>\n" +
                "    <out_trade_no>PAY202005262332274953679476</out_trade_no>\n" +
                "    <spbill_create_ip>192.168.2.199</spbill_create_ip>\n" +
                "    <total_fee>1</total_fee>\n" +
                "    <trade_type>APP</trade_type>\n" +
                "</xml>";
        String rspJsonStr = HttpUtil.doPostXml(urlEnum.getUrl(), reqJsonStr);
        return rspJsonStr;
    }
}
