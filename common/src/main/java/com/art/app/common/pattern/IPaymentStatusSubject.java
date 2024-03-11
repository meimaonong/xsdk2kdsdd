package com.art.app.common.pattern;

import com.art.app.common.pattern.entity.PaymentStatusParams;

public interface IPaymentStatusSubject {

    void accept(PaymentStatusParams params);
}
