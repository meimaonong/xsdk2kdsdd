package com.art.app.web.bean;

import com.art.app.common.bean.ErrorCode;
import com.art.app.common.enums.ResponseCodeEnum;
import lombok.Getter;

@Getter
public class BizException extends RuntimeException {

    protected final ErrorCode errorCode;

    public BizException() {
        super(ResponseCodeEnum.SYSTEM_ERROR.getMsg());
        this.errorCode = ResponseCodeEnum.SYSTEM_ERROR;
    }

    public BizException(final ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

    public BizException(final Throwable t) {
        super(t);
        this.errorCode = ResponseCodeEnum.SYSTEM_ERROR;
    }

    public BizException(final ErrorCode errorCode, final String detailedMessage) {
        super(detailedMessage);
        this.errorCode = errorCode;
    }

    public BizException(final ErrorCode errorCode, final Throwable t) {
        super(errorCode.getMsg(), t);
        this.errorCode = errorCode;
    }

    public BizException(final String detailedMessage, final Throwable t) {
        super(detailedMessage, t);
        this.errorCode = ResponseCodeEnum.SYSTEM_ERROR;
    }

    public BizException(final ErrorCode errorCode, final String detailedMessage,
                        final Throwable t) {
        super(detailedMessage, t);
        this.errorCode = errorCode;
    }

}
