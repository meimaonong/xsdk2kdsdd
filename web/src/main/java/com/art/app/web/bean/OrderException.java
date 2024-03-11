package com.art.app.web.bean;

import com.art.app.common.bean.ErrorCode;
import com.art.app.common.enums.ResponseCodeEnum;
import lombok.Getter;

@Getter
public class OrderException extends BizException {

    protected final ErrorCode errorCode;

    public OrderException() {
        super();
        this.errorCode = ResponseCodeEnum.SYSTEM_ERROR;
    }

    public OrderException(final ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public OrderException(final Throwable t) {
        super(t);
        this.errorCode = ResponseCodeEnum.SYSTEM_ERROR;
    }

    public OrderException(final ErrorCode errorCode, final String detailedMessage) {
        super(errorCode, detailedMessage);
        this.errorCode = errorCode;
    }

    public OrderException(final ErrorCode errorCode, final Throwable t) {
        super(errorCode.getMsg(), t);
        this.errorCode = errorCode;
    }

    public OrderException(final String detailedMessage, final Throwable t) {
        super(detailedMessage, t);
        this.errorCode = ResponseCodeEnum.SYSTEM_ERROR;
    }

    public OrderException(final ErrorCode errorCode, final String detailedMessage,
                          final Throwable t) {
        super(detailedMessage, t);
        this.errorCode = errorCode;
    }

}
