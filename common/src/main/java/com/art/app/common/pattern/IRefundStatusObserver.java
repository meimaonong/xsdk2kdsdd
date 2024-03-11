package com.art.app.common.pattern;

import com.art.app.common.pattern.entity.RefundStatusParams;

public interface IRefundStatusObserver {

    void deal(RefundStatusParams params);

    boolean support(RefundStatusParams params);
}
