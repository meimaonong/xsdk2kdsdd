package com.art.app.payment.service.impl;

import com.art.app.common.bean.FactoryList;
import com.art.app.common.enums.PaymentStatusEnum;
import com.art.app.common.enums.PaymentTypeEnum;
import com.art.app.orm.entity.PaymentOrderInfo;
import com.art.app.orm.entity.PaymentRefundOrderInfo;
import com.art.app.orm.entity.PaymentTypeManage;
import com.art.app.orm.service.PaymentOrderInfoService;
import com.art.app.orm.service.PaymentRefundOrderInfoService;
import com.art.app.orm.service.PaymentTypeManageService;
import com.art.app.payment.PaymentException;
import com.art.app.payment.annotation.RecordLog;
import com.art.app.payment.entity.PaymentTypeInfo;
import com.art.app.payment.entity.PrePayRequest;
import com.art.app.payment.entity.PrePayResponse;
import com.art.app.payment.entity.QueryPayResultRequest;
import com.art.app.payment.entity.QueryPayResultResponse;
import com.art.app.payment.entity.QueryRefundResultRequest;
import com.art.app.payment.entity.QueryRefundResultResponse;
import com.art.app.payment.entity.RefundRequest;
import com.art.app.payment.enums.PaymentErrorCodeEnum;
import com.art.app.payment.enums.RefundStatusEnum;
import com.art.app.payment.service.IPaymentService;
import com.art.app.payment.service.IPaymentTypeService;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements IPaymentService {

    @Resource
    private PaymentTypeManageService paymentTypeManageService;
    @Resource
    private FactoryList<Integer, IPaymentTypeService> iPaymentTypeServices;
    @Resource
    private PaymentOrderInfoService paymentOrderInfoService;
    @Resource
    private PaymentRefundOrderInfoService paymentRefundOrderInfoService;

    @Override
    @RecordLog
    public List<PaymentTypeInfo> queryAllPaymentTypes() {
        List<PaymentTypeInfo> results = null;
        Wrapper condition = Condition.create().eq("del_flag", 0);
        List<PaymentTypeManage> paymentTypeInfos = paymentTypeManageService.selectList(condition);
        if (!CollectionUtils.isEmpty(paymentTypeInfos)) {
            results = paymentTypeInfos.stream().filter(paymentTypeManage -> {
                return 1 == paymentTypeManage.getPaymentTypeStatus();
            }).filter(paymentTypeManage -> {
                return !PaymentTypeEnum.UNKNOWN.equals(PaymentTypeEnum.ofType(paymentTypeManage.getPaymentType()));
            }).map(paymentTypeManage -> {
                PaymentTypeInfo paymentTypeInfo = new PaymentTypeInfo();
                paymentTypeInfo.setPaymentType(paymentTypeManage.getPaymentType());
                paymentTypeInfo.setPaymentTypeName(PaymentTypeEnum.ofType(paymentTypeManage.getPaymentType()).getName());
                paymentTypeInfo.setStatus(paymentTypeManage.getPaymentTypeStatus());
                paymentTypeInfo.setRemark(paymentTypeManage.getRemark());
                return paymentTypeInfo;
            }).collect(Collectors.toList());
        } else {
            results = new ArrayList<>();
        }
        return results;
    }

    @Override
    @RecordLog
    public PrePayResponse prePay(PrePayRequest prePayRequest) {
        return iPaymentTypeServices.getBean(prePayRequest.getPaymentType()).prePay(prePayRequest);
    }

    @Override
    @RecordLog
    public QueryPayResultResponse queryPayResult(QueryPayResultRequest queryPayResultRequest, boolean checkSuccess) {
        Wrapper wrapper = Condition.create()
                .eq("art_order_id", queryPayResultRequest.getOrderId())
                .and().eq("del_flag", 0);
        List<PaymentOrderInfo> orderInfos = paymentOrderInfoService.selectList(wrapper);
        if (CollectionUtils.isEmpty(orderInfos)) {
            throw new PaymentException(PaymentErrorCodeEnum.PAYMENT_ORDER_NOT_EXIST);
        } else {
            QueryPayResultResponse payResultResponse = null;
            for (PaymentOrderInfo paymentOrderInfo : orderInfos) {
                payResultResponse = new QueryPayResultResponse();
                if (PaymentStatusEnum.isFinalStatus(paymentOrderInfo.getPaymentStatus())) {
                    payResultResponse.setResult(paymentOrderInfo.getPaymentStatus());
                    payResultResponse.setRemark(paymentOrderInfo.getPaymentStatusRemark());
                    payResultResponse.setOutOrderId(paymentOrderInfo.getOutOrderId());
                    if (checkSuccess && PaymentStatusEnum.PAY_SUCCESS.getStatus().equals(payResultResponse.getResult())) {
                        // 如果有支付成功，直接返回，否则看其他支付结果
                        return payResultResponse;
                    }
                } else {
                    payResultResponse = iPaymentTypeServices.getBean(paymentOrderInfo.getPaymentType()).queryPaymentTypePayResult(paymentOrderInfo);
                    if (checkSuccess && PaymentStatusEnum.PAY_SUCCESS.getStatus().equals(payResultResponse.getResult())) {
                        // 如果有支付成功，直接返回，否则看其他支付结果
                        return payResultResponse;
                    }
                }
            }
            return payResultResponse;
        }
    }

    @Override
    @RecordLog
    public void refund(RefundRequest refundRequest) {
        Wrapper wrapper = Condition.create()
                .eq("art_order_id", refundRequest.getOrderId())
                .and().eq("payment_status", PaymentStatusEnum.PAY_SUCCESS.getStatus())
                .and().eq(!StringUtils.isEmpty(refundRequest.getOutOrderId()), "out_order_id", refundRequest.getOutOrderId())
                .and().eq("del_flag", 0);
        PaymentOrderInfo paymentOrderInfo = paymentOrderInfoService.selectOne(wrapper);
        if (null == paymentOrderInfo) {
            throw new PaymentException(PaymentErrorCodeEnum.PAYMENT_ORDER_NOT_EXIST);
        } else {
            iPaymentTypeServices.getBean(paymentOrderInfo.getPaymentType()).refund(refundRequest, paymentOrderInfo);
        }
    }

    @Override
    @RecordLog
    public QueryRefundResultResponse queryRefundResult(QueryRefundResultRequest queryRefundResultRequest) {
        Wrapper wrapper = Condition.create()
                .eq("art_order_id", queryRefundResultRequest.getOrderId())
                .and().eq(!StringUtils.isEmpty(queryRefundResultRequest.getOutOrderId()), "out_order_id", queryRefundResultRequest.getOutOrderId())
                .and().eq("del_flag", 0);
        List<PaymentRefundOrderInfo> refundOrders = paymentRefundOrderInfoService.selectList(wrapper);
        if (CollectionUtils.isEmpty(refundOrders)) {
            throw new PaymentException(PaymentErrorCodeEnum.REFUND_ORDER_NOT_EXIST);
        } else {
            QueryRefundResultResponse resultResponse = null;
            for (PaymentRefundOrderInfo orderInfo : refundOrders) {
                resultResponse = new QueryRefundResultResponse();
                if (RefundStatusEnum.isFinalStatus(orderInfo.getRefundStatus())) {
                    resultResponse.setResult(orderInfo.getRefundStatus());
                    resultResponse.setRemark(orderInfo.getRefundStatusRemark());
                    resultResponse.setOutOrderId(orderInfo.getOutOrderId());
                    if (RefundStatusEnum.REFUND_SUCCESS.getRefundStatus().equals(resultResponse.getResult())) {
                        // 如果有退款成功，直接返回，否则看其他退款结果
                        return resultResponse;
                    }
                } else {
                    resultResponse = iPaymentTypeServices.getBean(orderInfo.getPaymentType()).queryRefundResult(orderInfo);
                    if (RefundStatusEnum.REFUND_SUCCESS.getRefundStatus().equals(resultResponse.getResult())) {
                        // 如果有退款成功，直接返回，否则看其他退款结果
                        return resultResponse;
                    }
                }
            }
            return resultResponse;
        }
    }

    @Override
    @RecordLog
    public String dealPayFeedback(HttpServletRequest request, Integer paymentType) throws IOException {
        return iPaymentTypeServices.getBean(paymentType).dealPayFeedback(request);
    }

    @Override
    @RecordLog
    public String dealRefundFeedback(HttpServletRequest request, Integer paymentType) throws IOException {
        return iPaymentTypeServices.getBean(paymentType).dealRefundFeedback(request);
    }
}
