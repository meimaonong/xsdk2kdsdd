package com.art.app.web.controller;

import com.art.app.common.enums.PaymentTypeEnum;
import com.art.app.payment.annotation.RecordLog;
import com.art.app.payment.entity.PaymentTypeInfo;
import com.art.app.payment.service.IPaymentService;
import com.art.app.web.annotation.LoginRequired;
import com.art.app.web.bean.response.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {

    @Resource
    private IPaymentService iPaymentService;

    @RequestMapping("/type/list/query")
    @LoginRequired
    public ResponseVo queryAllPaymentTypes() {
        List<PaymentTypeInfo> results = iPaymentService.queryAllPaymentTypes();
        return ResponseVo.successData(results);
    }

    /**
     * 微信支付通知
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/feedback/wechat/notify")
    @RecordLog
    public String wechatNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("收到微信支付结果通知");
        return iPaymentService.dealPayFeedback(request, PaymentTypeEnum.WECHAT.getType());
    }

    /**
     * 支付宝支付通知
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/feedback/alipay/notify")
    @RecordLog
    public String alipayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("收到支付宝支付结果通知");
        return iPaymentService.dealPayFeedback(request, PaymentTypeEnum.ALIPAY.getType());
    }

    /**
     * 微信退款通知
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/feedback/wechat/refund/notify")
    @RecordLog
    public String wechatRefundNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("收到微信退款结果通知");
        return iPaymentService.dealRefundFeedback(request, PaymentTypeEnum.WECHAT.getType());
    }

    /**
     * 支付宝退款通知
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/feedback/alipay/refund/notify")
    @RecordLog
    public String alipayRefundNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("收到支付宝退款结果通知");
        return iPaymentService.dealRefundFeedback(request, PaymentTypeEnum.ALIPAY.getType());
    }

}
