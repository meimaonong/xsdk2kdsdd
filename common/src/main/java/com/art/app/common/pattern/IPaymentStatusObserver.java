package com.art.app.common.pattern;

import com.art.app.common.pattern.entity.PaymentStatusParams;

public interface IPaymentStatusObserver {

    void deal(PaymentStatusParams params);

    boolean support(PaymentStatusParams params);
}
