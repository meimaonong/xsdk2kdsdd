package com.art.app.web.filter;

import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.web.bean.BizException;
import com.art.app.web.bean.OrderException;
import com.art.app.web.bean.response.ResponseVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GenericErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ResponseVo handleException(HttpServletRequest request, Exception ex) {

        int code = ResponseCodeEnum.SYSTEM_ERROR.getCode();
        if (ex instanceof HttpMessageNotReadableException) {
            code = ResponseCodeEnum.PARAM_ERROR.getCode();
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            code = ResponseCodeEnum.METHOD_ERROR.getCode();
        }
        String msg = null;

        if (ex instanceof BizException) {
            BizException bizException = (BizException) ex;
            msg = bizException.getMessage();
            code = bizException.getErrorCode().getCode();

        } else if (ex instanceof AccessDeniedException) {
            msg = ResponseCodeEnum.AUTH_FAIL.getMsg();
            code = ResponseCodeEnum.AUTH_FAIL.getCode();
        }
        ResponseVo resp = new ResponseVo();
        resp.setCode(code);
        resp.setMessage(msg);
        logger.error("code:{}, msg:{} ", code, StringUtils.isBlank(msg) ? ex.getMessage() : msg, ex);
        if (ex instanceof OrderException) {
            resp.setMessage(ResponseCodeEnum.SYSTEM_ERROR.getMsg() + "[" + code + "]");
        }
        return resp;
    }
}
