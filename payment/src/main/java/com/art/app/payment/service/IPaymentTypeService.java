package com.art.app.payment.service;

import com.art.app.common.bean.MatchingBean;
import com.art.app.orm.entity.PaymentOrderInfo;
import com.art.app.orm.entity.PaymentRefundOrderInfo;
import com.art.app.payment.entity.PrePayRequest;
import com.art.app.payment.entity.PrePayResponse;
import com.art.app.payment.entity.QueryPayResultResponse;
import com.art.app.payment.entity.QueryRefundResultResponse;
import com.art.app.payment.entity.RefundRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 根据paymentType实现接口
 */
public interface IPaymentTypeService extends MatchingBean<Integer> {

    PrePayResponse prePay(PrePayRequest prePayRequest);

    QueryPayResultResponse queryPaymentTypePayResult(PaymentOrderInfo paymentOrderInfo);

    void refund(RefundRequest refundRequest, PaymentOrderInfo paymentOrderInfo);

    QueryRefundResultResponse queryRefundResult(PaymentRefundOrderInfo refundOrderInfo);

    String dealPayFeedback(HttpServletRequest request) throws IOException;

    String dealRefundFeedback(HttpServletRequest request) throws IOException;
}
