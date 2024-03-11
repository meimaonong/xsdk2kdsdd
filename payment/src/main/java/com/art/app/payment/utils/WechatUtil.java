package com.art.app.payment.utils;

import com.art.app.payment.client.config.WechatPayConfig;
import com.art.app.payment.entity.wechat.WechatRefundFeedbackRequestDetail;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

@Slf4j
public class WechatUtil {

    /**
     * 验签
     *
     * @param map 入参
     * @param sign 入参签名
     * @param key 签名key
     * @return boolean
     */
    public static boolean verifySign(Map<String, Object> map, String sign, String key) {
        String content = getSignDataIgnoreNullObject(map);
        String objSign = sign(content, key);
        log.info("request sign -> {}, result sign -> {}", sign, objSign);
        return sign.equals(objSign);
    }

    /**
     * 生成签名
     */
    public static String sign(Map<String, Object> map, String key) {
        return sign(getSignDataIgnoreNullObject(map), key);
    }

    public static String createNonceStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 解密步骤如下：
     （1）对加密串A做base64解码，得到加密串B
     （2）对商户key做md5，得到32位小写key* ( key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置 )
     （3）用key*对加密串B做AES-256-ECB解密（PKCS7Padding）
     */
    public static String decryptReqInfo(String reqInfo, String key) {
        String md5Key = null;
        try {
            md5Key = DigestUtils.md5Hex(key.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return AesECBUtil.decryptData(reqInfo, md5Key);
    }

    private static String sign(String content, String key) {
        String sign = null;
        try {
            sign = sign(content, key, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            log.error("加签失败");
        }
        return sign;
    }

    private static String sign(String content, String key, String charset) throws Exception {
        log.info("sign content (without key) -> {}", content);
        String tosign = (content == null ? "" : content) + "&key=" + key;
        try {
            return DigestUtils.md5Hex(getContentBytes(tosign, charset)).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            throw new SignatureException(" MD5 Exception [content = " + content + "; charset = utf-8" + "]Exception!",
                    e);
        }
    }

    private static byte[] getContentBytes(String content, String charset) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(charset)) {
            return content.getBytes(StandardCharsets.UTF_8);
        }
        return content.getBytes(charset);
    }

    /**
     * 忽略掉Null的值。
     */
    private static String getSignDataIgnoreNullObject(Map<String, Object> params) {
        SortedMap<String, Object> sortedMap = new TreeMap<>();
        sortedMap.putAll(params);
        StringBuilder content = new StringBuilder();
        Set es = sortedMap.entrySet();
        for (Object e : es) {
            Map.Entry entry = (Map.Entry) e;
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if (null != value && !"".equals(value) && !"sign".equals(key) && !"key".equals(key)) {
                if (content.length() > 0) {
                    content.append("&");
                }
                content.append(key).append("=").append(value);
            }
        }
        return content.toString();
    }

    public static void main(String[] args) {
        String str="wvm81DNoEyMiBbF7gjHh51JWMpdVsxwGuQ/Ej4DcGR408/bJzsu0zsCGcm9t3OMuDCoI+oXQKZBX/iBFo/+TfOf0w8YMbeDZELHUBShN+Pw7It0rzsBY0/ScN9EFRAg9uFE5AKFzlEMdDnHdEk5IX8bn+q/iNyuZ20Zfatx/n1u1r/+IDKtrxUDyPpRxzeHlc0eIzN7tNfnW+BjWenmlK+/qzqb58s8VEJ8vwKjXnIuBRUnt8IXouCqR4pZjQ7VY/pasfJcb8wBvCMOJ8szpoZv2kB0UglhQ/f8VgMNVzSd7Jlbr/Gvikb4reFPoZuSQgCkL0wcT1txmPqF2hNd7Ct91Gp3CYwjGo1DSIl6UVedPYb9XRh2vhUkmn1L9ri4VtXvKxMZeda+gzcpzFkQtroQUHCtKvWYyDUzTIDT2uLvyusZLPrhI1t3r7Gqu4b7J9aGADUuCh4CmeoR5eIcOoagtiNcrd6H5HBnYuqHCw6mf4/9/+3E60SOkiTr2hhijwkaqif7r2jpwluHBZ2bxQ896Lnlh7WiwFZ6KxVL9TwKTUa0EvMW+xcT7Sh4hawjgEuZCzOjixNl1tcfqwBGxJDMHODTV2GBX7uHntPCGfiMbuuU/0CVn5VGuJETQXmOM7KT6kaK22TCJj7d3yw7dtROSJ2WIg7O5ieodfHYSrBlw3ZLnaLUx63K0BcJCrNUYJ8tO28x3swgVrNZHcQccsR/35rdrUuyd2E1CCdFW2/XnniY8ZJSrd33Xy+9dCjR5almlJwyLWAXHTIQJHMQjs5yhBNL9PJRYk9lxb3DulIAS37fHEzKiuNm8aA46SMz7EsDnTYdkPA7I+vf3GED6IiKeTjLD4z/LBN2FvUXxwmInaGAZJjZeWUa3cUSJCR+hyxRvAcUwWr9w1Yu+KHqXIAbMdrPYO+SCnK9nDBOVO/JL62Nb5gV5xpAIpeJW9dvABDPpEJyxtPTmL3J+5KPF0fNt678eLbYHJdp731j3JojVYvNx++eDJGg9LxLT88ECqFX1II+x2FiCSt2MG2JTR1OlWCtOMrpO1e5lzg0ScSU=";
        String key = "3c6e0b8a9c15224a8228b9a98ca1531d";
        String s = decryptReqInfo(str, key);
        System.out.println(s);
    }

}
