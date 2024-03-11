package com.art.app.web.controller;

import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.payment.annotation.RecordLog;
import com.art.app.web.annotation.LoginRequired;
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
import com.art.app.web.bean.response.ResponseVo;
import com.art.app.web.bean.response.order.ClassOrderResult;
import com.art.app.web.bean.response.order.ExhibitionOrderResult;
import com.art.app.web.bean.response.order.MemberOrderResult;
import com.art.app.web.bean.response.order.PayResultQueryResult;
import com.art.app.web.bean.response.order.PrePayResult;
import com.art.app.web.bean.response.order.RefundResultQueryResult;
import com.art.app.web.bean.response.order.SketchingOrderResult;
import com.art.app.web.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 高研班报名
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("/class/apply")
    @LoginRequired
    @RecordLog
    public ResponseVo applyClass(@RequestBody OrderRequestVo<ClassOrderBizParam> requestVo) {
        ClassOrderResult result = orderService.applyClass(requestVo);
        return ResponseVo.successData(result);
    }

    /**
     * 艺术展览报名
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("/exhibition/apply")
    @LoginRequired
    @RecordLog
    public ResponseVo applyExhibition(@RequestBody OrderRequestVo<ExhibitionOrderBizParam> requestVo) {
        ExhibitionOrderResult result = orderService.applyExhibition(requestVo);
        return ResponseVo.successData(result);
    }

    /**
     * 活动报名
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("/sketching/apply")
    @LoginRequired
    @RecordLog
    public ResponseVo applySketching(@RequestBody OrderRequestVo<SketchingOrderBizParam> requestVo) {
        SketchingOrderResult result = orderService.applySketching(requestVo);
        return ResponseVo.successData(result);
    }

    /**
     * 购买会员
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("/member/apply")
    @LoginRequired
    @RecordLog
    public ResponseVo applyMember(@RequestBody OrderRequestVo<MemberOrderBizParam> requestVo) {
        MemberOrderResult result = orderService.applyMember(requestVo);
        return ResponseVo.successData(result);
    }

    /**
     * 预支付
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("/pre-pay")
    @LoginRequired
    @RecordLog
    public ResponseVo prePay(@RequestBody OrderRequestVo<PrePayBizParam> requestVo) {
        PrePayBizParam orderBizParams = requestVo.getOrderBizParams(PrePayBizParam.class);
        if (null == orderBizParams
                || StringUtils.isEmpty(orderBizParams.getOrderId())
                || null == orderBizParams.getPaymentType()) {
            throw new OrderException(ResponseCodeEnum.PARAM_ERROR);
        } else {
            PrePayResult result = orderService.prePay(requestVo);
            return ResponseVo.successData(result);
        }
    }

    /**
     * 查询支付结果
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("/pay/result/query")
    @LoginRequired
    @RecordLog
    public ResponseVo queryPayResult(@RequestBody OrderRequestVo<PayResultQueryBizParam> requestVo) {
        PayResultQueryBizParam orderBizParams = requestVo.getOrderBizParams(PayResultQueryBizParam.class);
        if (null == orderBizParams
                || StringUtils.isEmpty(orderBizParams.getOrderId())) {
            throw new OrderException(ResponseCodeEnum.PARAM_ERROR);
        } else {
            PayResultQueryResult result = orderService.queryPayResult(requestVo);
            return ResponseVo.successData(result);
        }
    }

    @RequestMapping("/refund/apply")
    @LoginRequired
    @RecordLog
    public ResponseVo refundApply(@RequestBody OrderRequestVo<RefundApplyBizParam> requestVo) {
        RefundApplyBizParam bizParams = requestVo.getOrderBizParams(RefundApplyBizParam.class);
        if (null == bizParams || StringUtils.isEmpty(bizParams.getOrderId())) {
            throw new OrderException(ResponseCodeEnum.PARAM_ERROR);
        } else {
            orderService.refundApply(requestVo);
            return ResponseVo.successData(null);
        }
    }

    @RequestMapping("/refund/result/query")
    @LoginRequired
    @RecordLog
    public ResponseVo queryRefundResult(@RequestBody OrderRequestVo<RefundResultQueryBizParam> requestVo) {
        RefundResultQueryBizParam bizParams = requestVo.getOrderBizParams(RefundResultQueryBizParam.class);
        if (null == bizParams || StringUtils.isEmpty(bizParams.getOrderId())) {
            throw new OrderException(ResponseCodeEnum.PARAM_ERROR);
        } else {
            RefundResultQueryResult result = orderService.queryRefundResult(requestVo);
            return ResponseVo.successData(result);
        }
    }

}
