package com.art.app.payment.service.impl;

import com.art.app.common.pattern.IPaymentStatusObserver;
import com.art.app.common.pattern.IPaymentStatusSubject;
import com.art.app.common.pattern.entity.PaymentStatusParams;
import com.art.app.payment.utils.JackJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class PaymentStatusSubjectImpl implements IPaymentStatusSubject {

    @Resource
    private List<IPaymentStatusObserver> iPaymentStatusObservers;

    @Override
    public void accept(PaymentStatusParams params) {
        log.info("支付订单[{}]状态变更 -> {}, ", params.getPaymentOrderId(), JackJsonUtil.toJson(params));
        if (!CollectionUtils.isEmpty(iPaymentStatusObservers)) {
            iPaymentStatusObservers.forEach(observer -> {
                if (observer.support(params)) {
                    observer.deal(params);
                }
            });
        }
    }
}
