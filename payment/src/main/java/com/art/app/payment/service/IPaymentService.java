package com.art.app.payment.service;

import com.art.app.payment.entity.PaymentTypeInfo;
import com.art.app.payment.entity.PrePayRequest;
import com.art.app.payment.entity.PrePayResponse;
import com.art.app.payment.entity.QueryPayResultRequest;
import com.art.app.payment.entity.QueryPayResultResponse;
import com.art.app.payment.entity.QueryRefundResultRequest;
import com.art.app.payment.entity.QueryRefundResultResponse;
import com.art.app.payment.entity.RefundRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 支付模块对外接口
 */
public interface IPaymentService {

    List<PaymentTypeInfo> queryAllPaymentTypes();

    PrePayResponse prePay(PrePayRequest prePayRequest);

    QueryPayResultResponse queryPayResult(QueryPayResultRequest queryPayResultRequest, boolean checkSuccess);

    void refund(RefundRequest refundRequest);

    QueryRefundResultResponse queryRefundResult(QueryRefundResultRequest queryRefundResultRequest);

    String dealPayFeedback(HttpServletRequest request, Integer paymentType) throws IOException;

    String dealRefundFeedback(HttpServletRequest request, Integer paymentType) throws IOException;
}
