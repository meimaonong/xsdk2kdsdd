package com.art.app.web.service;

import com.art.app.common.enums.OrderPaymentStatusEnum;
import com.art.app.common.pattern.IRefundStatusObserver;
import com.art.app.common.pattern.entity.RefundStatusParams;
import com.art.app.payment.enums.RefundStatusEnum;
import com.art.app.payment.utils.JackJsonUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRefundStatusObserver implements IRefundStatusObserver {

    @Override
    public void deal(RefundStatusParams params) {
        OrderPaymentStatusEnum orderPaymentStatusEnum = convertOrderPaymentStatusEnum(params.getRefundStatus());
        if (OrderPaymentStatusEnum.REFUND.getStatus().equals(orderPaymentStatusEnum.getStatus())) {
            updateOrderPaymentStatus(params, orderPaymentStatusEnum);
        } else {
            log.info("退款失败 -> {}", JackJsonUtil.toJson(params));
            // TODO 提醒退款失败

        }
    }

    protected abstract void updateOrderPaymentStatus(RefundStatusParams params, OrderPaymentStatusEnum orderPaymentStatusEnum);

    private OrderPaymentStatusEnum convertOrderPaymentStatusEnum(Integer refundStatus) {
        OrderPaymentStatusEnum result = null;
        if (RefundStatusEnum.REFUND_SUCCESS.getRefundStatus().equals(refundStatus)) {
            result = OrderPaymentStatusEnum.REFUND;
        } else {
            result = OrderPaymentStatusEnum.UNKNOWN;
        }
        return result;
    }
}
