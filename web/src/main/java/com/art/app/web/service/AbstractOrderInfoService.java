package com.art.app.web.service;

import com.art.app.common.Constants;
import com.art.app.common.enums.OrderPaymentStatusEnum;
import com.art.app.common.enums.OrderStatusEnum;
import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.common.util.TokenUtils;
import com.art.app.orm.entity.TrainingClass;
import com.art.app.orm.entity.XslOrderInfo;
import com.art.app.orm.service.XslOrderInfoService;
import com.art.app.payment.entity.PrePayRequest;
import com.art.app.web.bean.BizException;
import com.art.app.web.bean.OrderBasicInfo;
import com.art.app.web.bean.request.activity.ActivitySubListBizParam;
import com.art.app.web.bean.request.order.OrderRequestVo;
import com.art.app.web.bean.request.order.PrePayBizParam;
import com.art.app.web.bean.response.order.OrderResult;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public abstract class AbstractOrderInfoService implements IOrderInfoService {

    @Resource
    private XslOrderInfoService xslOrderInfoService;

    @Override
    public OrderResult applyOrder(OrderRequestVo requestVo) {
        int userId = TokenUtils.getUserIdByToken(requestVo.getBaseParams().getToken());
        XslOrderInfo xslOrderInfo = buildXslOrderInfo(requestVo, userId);
        xslOrderInfoService.insert(xslOrderInfo);
        saveOrderDetailInfo(requestVo, userId, xslOrderInfo.getOrderId());
        OrderResult orderResult = new OrderResult();
        orderResult.setOrderId(xslOrderInfo.getOrderId());
        orderResult.setAmount(xslOrderInfo.getAmount());
        return orderResult;
    }

    @Override
    public PrePayRequest buildPrePayRequest(XslOrderInfo xslOrderInfo, PrePayBizParam orderBizParams) {
        PrePayRequest prePayRequest = new PrePayRequest();
        prePayRequest.setOrderId(xslOrderInfo.getOrderId());
        prePayRequest.setOrderType(xslOrderInfo.getOrderType());
        prePayRequest.setAmount(xslOrderInfo.getAmount());
        prePayRequest.setPaymentType(orderBizParams.getPaymentType());
        OrderBasicInfo basicInfo = queryOrderBasicInfo(xslOrderInfo.getOrderId());
        Date nowDate = DatetimeUtils.now();
        if (OrderPaymentStatusEnum.NOT_PAID.getStatus().equals(basicInfo.getPaymentStatus())
                && !DatetimeUtils.addMinute(nowDate, -Constants.MAX_PAYMENT_TIME).before(basicInfo.getCreatedAt())) {
            // 如果订单在前60分钟没有支付过，抛出异常，禁止支付
            throw new BizException(ResponseCodeEnum.ORDER_LATESET_PAY_TIME_ERROR);
        }
        prePayRequest.setSubject(basicInfo.getSubject());
        prePayRequest.setTimeExpire(basicInfo.getTimeExpire());
        return prePayRequest;
    }

    protected Integer convertActivityStatus(Integer orderStatus, Integer paymentStatus,
                                            Date applyStartTime, Date applyStopTime) {
        if (OrderPaymentStatusEnum.SUCCESS.getStatus().equals(paymentStatus)
                || OrderPaymentStatusEnum.REFUND.getStatus().equals(paymentStatus)) {
            // 已完成:完成了支付
            return 1;
        } else {
            Date nowDate = DatetimeUtils.now();
            if ((null == applyStartTime || nowDate.after(applyStartTime))
                    && (null == applyStopTime || nowDate.before(applyStopTime))) {
                if (!OrderPaymentStatusEnum.isFinalStatus(paymentStatus)) {
                    // 进行中: 在有效期，未完成支付
                    return 0;
                }
            }
        }
        // 其他情况返回 2
        return 2;
    }

    protected Wrapper convertCountWrapper(ActivitySubListBizParam bizParams, int userId) {
        List<Integer> orderPaymentStatusList = Arrays.asList(OrderPaymentStatusEnum.SUCCESS.getStatus(),
                OrderPaymentStatusEnum.REFUND.getStatus());
        Wrapper wrapper = Condition.create()
                .eq("user_id", userId)
                .and().eq("order_status", OrderStatusEnum.APPLY_SUCCESS.getType())
                .and().in("payment_status", orderPaymentStatusList)
                .and().eq("del_flag", 0);
        return wrapper;
    }

    protected Wrapper convertListWrapper(ActivitySubListBizParam bizParams, int start, int userId) {
        List<Integer> orderPaymentStatusList = Arrays.asList(OrderPaymentStatusEnum.SUCCESS.getStatus(),
                OrderPaymentStatusEnum.REFUND.getStatus());
        Wrapper wrapper = Condition.create()
                .eq("user_id", userId)
                .and().eq("order_status", OrderStatusEnum.APPLY_SUCCESS.getType())
                .and().in("payment_status", orderPaymentStatusList)
                .and().eq("del_flag", 0)
                .last("limit " + start + "," + bizParams.getPageSize());
        return wrapper;
    }

    protected abstract XslOrderInfo buildXslOrderInfo(OrderRequestVo requestVo, int userId);

    protected abstract void saveOrderDetailInfo(OrderRequestVo requestVo, int userId, String orderId);


}
