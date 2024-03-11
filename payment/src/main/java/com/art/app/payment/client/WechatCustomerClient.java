package com.art.app.payment.client;

import com.art.app.common.util.HttpUtil;
import com.art.app.common.util.XmlUtils;
import com.art.app.payment.PaymentException;
import com.art.app.payment.annotation.RecordLog;
import com.art.app.payment.client.config.WechatPayConfig;
import com.art.app.payment.entity.wechat.CommonRequestParams;
import com.art.app.payment.entity.wechat.CommonResponseParams;
import com.art.app.payment.entity.wechat.RequestLogParams;
import com.art.app.payment.enums.PaymentErrorCodeEnum;
import com.art.app.payment.enums.WechatPayUrlEnum;
import com.art.app.payment.utils.JackJsonUtil;
import com.art.app.payment.utils.WechatUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class WechatCustomerClient {

    @RecordLog
    public <T extends CommonRequestParams, K extends CommonResponseParams> K process(T requestParam, WechatPayUrlEnum urlEnum,
                                                                                     Class<K> responseParam) {
        return process(requestParam, urlEnum, responseParam, new RequestLogParams());
    }

    @RecordLog
    public <T extends CommonRequestParams, K extends CommonResponseParams> K process(T requestParam, WechatPayUrlEnum urlEnum,
                                                                                     Class<K> responseParam, RequestLogParams requestLogParams) {
        requestParam.setAppId(WechatPayConfig.getWechatAppId());
        requestParam.setMchId(WechatPayConfig.getWechatMchId());
        requestParam.setNonceStr(WechatUtil.createNonceStr());
        requestParam.setSign(createSign(requestParam));
        // 解决报文中有中文，微信会返回验签失败的问题
        String reqXmlStr = XmlUtils.object2Xml(requestParam, "ISO8859-1");
        log.info("request -> {}", reqXmlStr);
        requestLogParams.setRequestParam(reqXmlStr);
        String responseXml = "";
        String errorMsg = "";
        try {
            responseXml = HttpUtil.doPostXml(urlEnum.getUrl(), reqXmlStr);
            log.info("response -> {}", responseXml);
        } catch (Exception e) {
            requestLogParams.setResponseError(true);
            errorMsg = StringUtils.left(ExceptionUtils.getStackTrace(e), 1000);
            throw new PaymentException(PaymentErrorCodeEnum.WECHAT_PAY_REQUEST_ERROR, e);
        } finally {
            requestLogParams.setResponseParam(StringUtils.isEmpty(errorMsg) ? responseXml : errorMsg);
        }
        return convertResponseParams(responseXml, responseParam);
    }

    private <T extends CommonRequestParams> String createSign(T requestParam) {
        Map<String, Object> requestMap = JackJsonUtil.toBean(JackJsonUtil.toJson(requestParam), new TypeReference<Map<String, Object>>() {
        });
        return WechatUtil.sign(requestMap, WechatPayConfig.getWechatApiKey());
    }

    private static <K extends CommonResponseParams> boolean verifySign(K responseParams, String apiKey) {
        String responseJsonStr = JackJsonUtil.toJson(responseParams);
        Map<String, Object> responseMap = JackJsonUtil.toBean(responseJsonStr, new TypeReference<Map<String, Object>>() {
        });
        return WechatUtil.verifySign(responseMap, responseParams.getSign(), apiKey);
    }

    private static <K extends CommonResponseParams> K convertResponseParams(String responseXml, Class<K> responseParam) {
        K reponseParams = XmlUtils.xml2Object(responseXml, responseParam);
        if (null != reponseParams) {
            if ("SUCCESS".equals(reponseParams.getReturnCode())) {
                if (verifySign(reponseParams, WechatPayConfig.getWechatApiKey())) {
                    if (!"SUCCESS".equals(reponseParams.getResultCode())) {
                        if ("ORDERNOTEXIST".equals(reponseParams.getErrCode())) {
                            throw new PaymentException(PaymentErrorCodeEnum.OUT_TRADE_NO_NOT_EXIST,
                                    reponseParams.getErrCode() + "-" + reponseParams.getErrCodeDes());
                        } else {
                            throw new PaymentException(PaymentErrorCodeEnum.WECHAT_PAY_RESPONSE_RESULT_CODE_ERROR,
                                    reponseParams.getErrCode() + "-" + reponseParams.getErrCodeDes());
                        }
                    }
                    return reponseParams;
                } else {
                    // 签名验证不通过
                    throw new PaymentException(PaymentErrorCodeEnum.WECHAT_PAY_RESPONSE_SIGN_ERROR);
                }
            } else {
                // 返回码错误
                throw new PaymentException(PaymentErrorCodeEnum.WECHAT_PAY_RESPONSE_RETURN_CODE_ERROR);
            }
        } else {
            // xml解析错误
            throw new PaymentException(PaymentErrorCodeEnum.WECHAT_PAY_RESPONSE_XML_ERROR);
        }
    }

}
