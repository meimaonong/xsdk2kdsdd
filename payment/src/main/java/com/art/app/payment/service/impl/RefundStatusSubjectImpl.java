package com.art.app.payment.service.impl;

import com.art.app.common.pattern.IPaymentStatusSubject;
import com.art.app.common.pattern.IRefundStatusObserver;
import com.art.app.common.pattern.IRefundStatusSubject;
import com.art.app.common.pattern.entity.PaymentStatusParams;
import com.art.app.common.pattern.entity.RefundStatusParams;
import com.art.app.payment.utils.JackJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class RefundStatusSubjectImpl implements IRefundStatusSubject {

    @Resource
    private List<IRefundStatusObserver> iRefundStatusObservers;

    @Override
    public void accept(RefundStatusParams params) {
        log.info("退款订单[{}]状态变更 -> {}, ", params.getRefundOrderId(), JackJsonUtil.toJson(params));
        if (!CollectionUtils.isEmpty(iRefundStatusObservers)) {
            iRefundStatusObservers.forEach(observer -> {
                if (observer.support(params)) {
                    observer.deal(params);
                }
            });
        }
    }
}
