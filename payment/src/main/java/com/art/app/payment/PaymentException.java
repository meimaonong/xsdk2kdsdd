package com.art.app.payment;

import com.art.app.payment.enums.PaymentErrorCodeEnum;

public class PaymentException extends RuntimeException {

	private static final long serialVersionUID = -199275355557737785L;

	private Integer code;

	public PaymentException(Integer code, String msg) {
		super(msg);
		this.code = code;
	}

	public PaymentException(PaymentErrorCodeEnum errorCode, String message) {
		super(errorCode.getMsg() + " : " + message);
		this.setCode(errorCode.getCode());
	}

	public PaymentException(PaymentErrorCodeEnum errorCode, Exception e) {
		super(errorCode.getMsg() + " : " + e.getMessage(), e);
		this.setCode(errorCode.getCode());
	}

	public PaymentException(PaymentErrorCodeEnum errorCodeEnum) {
		super(errorCodeEnum.getMsg());
		this.code = errorCodeEnum.getCode();
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
