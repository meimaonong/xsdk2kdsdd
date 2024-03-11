package com.art.app.payment.client;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.art.app.payment.PaymentException;
import com.art.app.payment.annotation.RecordLog;
import com.art.app.payment.enums.PaymentErrorCodeEnum;

public class AlipayCustomerClient extends DefaultAlipayClient {

    public AlipayCustomerClient(String serverUrl, String appId, String privateKey) {
        super(serverUrl, appId, privateKey);
    }

    public AlipayCustomerClient(String serverUrl, String appId, String privateKey, String format) {
        super(serverUrl, appId, privateKey, format);
    }

    public AlipayCustomerClient(String serverUrl, String appId, String privateKey, String format, String charset) {
        super(serverUrl, appId, privateKey, format, charset);
    }

    public AlipayCustomerClient(String serverUrl, String appId, String privateKey, String format, String charset, String alipayPublicKey) {
        super(serverUrl, appId, privateKey, format, charset, alipayPublicKey);
    }

    public AlipayCustomerClient(String serverUrl, String appId, String privateKey, String format, String charset, String alipayPublicKey, String signType) {
        super(serverUrl, appId, privateKey, format, charset, alipayPublicKey, signType);
    }

    public AlipayCustomerClient(String serverUrl, String appId, String privateKey, String format, String charset, String alipayPublicKey, String signType, String proxyHost, int proxyPort) {
        super(serverUrl, appId, privateKey, format, charset, alipayPublicKey, signType, proxyHost, proxyPort);
    }

    public AlipayCustomerClient(String serverUrl, String appId, String privateKey, String format, String charset, String alipayPublicKey, String signType, String encryptKey, String encryptType) {
        super(serverUrl, appId, privateKey, format, charset, alipayPublicKey, signType, encryptKey, encryptType);
    }

    public AlipayCustomerClient(CertAlipayRequest certAlipayRequest) throws AlipayApiException {
        super(certAlipayRequest);
    }

    @RecordLog
    public <T extends AlipayResponse> T sdkExecute(AlipayRequest<T> request) throws AlipayApiException {
        T result = super.sdkExecute(request);
        if (result.isSuccess()) {
            return result;
        } else {
            throw new PaymentException(PaymentErrorCodeEnum.ALIPAY_REQUEST_ERROR);
        }
    }

    @RecordLog
    public <T extends AlipayResponse> T execute(AlipayRequest<T> request) throws AlipayApiException {
        T result = super.execute(request);
        if (result.isSuccess()) {
            return result;
        } else {
            if ("ACQ.TRADE_NOT_EXIST".equals(result.getSubCode())) {
                throw new PaymentException(PaymentErrorCodeEnum.OUT_TRADE_NO_NOT_EXIST,
                        result.getSubCode() + "-" + result.getSubMsg());
            }
            throw new PaymentException(PaymentErrorCodeEnum.ALIPAY_REQUEST_ERROR);
        }
    }
}
