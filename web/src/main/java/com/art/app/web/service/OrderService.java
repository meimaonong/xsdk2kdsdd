package com.art.app.web.service;

import com.art.app.common.bean.FactoryList;
import com.art.app.common.enums.OrderPaymentStatusEnum;
import com.art.app.common.enums.OrderTypeEnum;
import com.art.app.common.enums.PaymentTypeEnum;
import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.orm.entity.XslOrderInfo;
import com.art.app.orm.service.XslOrderInfoService;
import com.art.app.payment.entity.PrePayRequest;
import com.art.app.payment.entity.PrePayResponse;
import com.art.app.payment.entity.QueryPayResultRequest;
import com.art.app.payment.entity.QueryPayResultResponse;
import com.art.app.payment.entity.QueryRefundResultRequest;
import com.art.app.payment.entity.QueryRefundResultResponse;
import com.art.app.payment.entity.RefundRequest;
import com.art.app.payment.service.IPaymentService;
import com.art.app.web.bean.OrderException;
import com.art.app.web.bean.request.order.ClassOrderBizParam;
import com.art.app.web.bean.request.order.ExhibitionOrderBizParam;
import com.art.app.web.bean.request.order.MemberOrderBizParam;
import com.art.app.web.bean.request.order.OrderRequestVo;
import com.art.app.web.bean.request.order.PayResultQueryBizParam;
import com.art.app.web.bean.request.order.PrePayBizParam;
import com.art.app.web.bean.request.order.RefundApplyBizParam;
import com.art.app.web.bean.request.order.RefundResultQueryBizParam;
import com.art.app.web.bean.request.order.SketchingOrderBizParam;
import com.art.app.web.bean.response.order.ClassOrderResult;
import com.art.app.web.bean.response.order.ExhibitionOrderResult;
import com.art.app.web.bean.response.order.MemberOrderResult;
import com.art.app.web.bean.response.order.OrderResult;
import com.art.app.web.bean.response.order.PayResultQueryResult;
import com.art.app.web.bean.response.order.PrePayResult;
import com.art.app.web.bean.response.order.RefundResultQueryResult;
import com.art.app.web.bean.response.order.SketchingOrderResult;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;

@Service
@Slf4j
public class OrderService {

    @Resource
    private FactoryList<Integer, IOrderInfoService> iOrderInfoServices;
    @Resource
    private XslOrderInfoService xslOrderInfoService;
    @Resource
    private IPaymentService iPaymentService;


    public ClassOrderResult applyClass(OrderRequestVo<ClassOrderBizParam> requestVo) {
        OrderResult orderResult = iOrderInfoServices.getBean(OrderTypeEnum.CLASS.getType()).applyOrder(requestVo);
        ClassOrderResult result = new ClassOrderResult();
        result.setOrderId(orderResult.getOrderId());
        result.setAmount(orderResult.getAmount());
        return result;
    }

    public ExhibitionOrderResult applyExhibition(OrderRequestVo<ExhibitionOrderBizParam> requestVo) {
        OrderResult orderResult = iOrderInfoServices.getBean(OrderTypeEnum.EXHIBITION.getType()).applyOrder(requestVo);
        ExhibitionOrderResult result = new ExhibitionOrderResult();
        result.setOrderId(orderResult.getOrderId());
        result.setAmount(orderResult.getAmount());
        return result;
    }

    public SketchingOrderResult applySketching(OrderRequestVo<SketchingOrderBizParam> requestVo) {
        OrderResult orderResult = iOrderInfoServices.getBean(OrderTypeEnum.SKETCHING.getType()).applyOrder(requestVo);
        SketchingOrderResult result = new SketchingOrderResult();
        result.setOrderId(orderResult.getOrderId());
        result.setAmount(orderResult.getAmount());
        return result;
    }

    public MemberOrderResult applyMember(OrderRequestVo<MemberOrderBizParam> requestVo) {
        OrderResult orderResult = iOrderInfoServices.getBean(OrderTypeEnum.MEMBER.getType()).applyOrder(requestVo);
        MemberOrderResult result = new MemberOrderResult();
        result.setOrderId(orderResult.getOrderId());
        result.setAmount(orderResult.getAmount());
        return result;
    }

    public PrePayResult prePay(OrderRequestVo<PrePayBizParam> requestVo) {
        PrePayBizParam orderBizParams = requestVo.getOrderBizParams(PrePayBizParam.class);
        if (PaymentTypeEnum.UNKNOWN.equals(PaymentTypeEnum.ofType(orderBizParams.getPaymentType()))) {
            throw new OrderException(ResponseCodeEnum.PARAM_ERROR);
        }
        String orderId = orderBizParams.getOrderId();
        Wrapper wrapper = Condition.create()
                .eq("order_id", orderId)
                .and().eq("del_flag", 0)
                .orderDesc(Collections.singleton("id"))
                .last("limit 1");
        XslOrderInfo xslOrderInfo = xslOrderInfoService.selectOne(wrapper);
        if (null == xslOrderInfo) {
            throw new OrderException(ResponseCodeEnum.PARAM_ERROR);
        }
        PrePayRequest prePayRequest = iOrderInfoServices.getBean(xslOrderInfo.getOrderType()).buildPrePayRequest(xslOrderInfo, orderBizParams);
        PrePayResponse prePayResponse = iPaymentService.prePay(prePayRequest);
        // 更新订单支付状态为支付中
        iOrderInfoServices.getBean(xslOrderInfo.getOrderType()).updateOrderPaymentStatus(xslOrderInfo, OrderPaymentStatusEnum.NOT_PAID, OrderPaymentStatusEnum.PAYING);
        PrePayResult result = new PrePayResult();
        result.setPaymentType(prePayResponse.getPaymentType());
        result.setParams(prePayResponse.getParams());
        return result;
    }

    public PayResultQueryResult queryPayResult(OrderRequestVo<PayResultQueryBizParam> requestVo) {
        QueryPayResultRequest payResultRequest = new QueryPayResultRequest();
        payResultRequest.setOrderId(requestVo.getOrderBizParams(PayResultQueryBizParam.class).getOrderId());
        QueryPayResultResponse payResultResponse = iPaymentService.queryPayResult(payResultRequest, true);
        PayResultQueryResult result = new PayResultQueryResult();
        result.setResult(payResultResponse.getResult());
        result.setRemark(payResultResponse.getRemark());
        return result;
    }

    public void refundApply(OrderRequestVo<RefundApplyBizParam> requestVo) {
        RefundApplyBizParam bizParams = requestVo.getOrderBizParams(RefundApplyBizParam.class);
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderId(bizParams.getOrderId());
        refundRequest.setRefundReason(bizParams.getRefundReason());
        iPaymentService.refund(refundRequest);
    }

    public RefundResultQueryResult queryRefundResult(OrderRequestVo<RefundResultQueryBizParam> requestVo) {
        RefundResultQueryBizParam bizParams = requestVo.getOrderBizParams(RefundResultQueryBizParam.class);
        QueryRefundResultRequest refundResultRequest = new QueryRefundResultRequest();
        refundResultRequest.setOrderId(bizParams.getOrderId());
        QueryRefundResultResponse refundResultResponse = iPaymentService.queryRefundResult(refundResultRequest);
        RefundResultQueryResult result = new RefundResultQueryResult();
        result.setResult(refundResultResponse.getResult());
        result.setRemark(refundResultResponse.getRemark());
        return result;
    }
}
