package com.art.app.payment.utils;

import com.art.app.payment.client.config.WechatPayConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Security;

@Slf4j
public class AesECBUtil {

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS7Padding";

    static {
        //Security.addProvider(new BouncyCastleProvider());
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * AES加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptData(String data, String key) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING, "BC");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
            // 初始化
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] bytes = cipher.doFinal(data.getBytes());
            return new Base64().encodeToString(bytes);
        } catch(Exception e) {
            log.error("AesECB Encrypt exception", e);
            return null;
        }
    }

    /**
     * AES解密
     *
     * @param base64Data
     * @return
     * @throws Exception
     */
    public static String decryptData(String base64Data, String key) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING, "BC");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] bytes = cipher.doFinal(new Base64().decode(base64Data));
            return new String(bytes,"utf-8");
        } catch (Exception ex) {
            log.info("AesECB Decrypt exception", ex);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String A="wvm81DNoEyMiBbF7gjHh51JWMpdVsxwGuQ/Ej4DcGR408/bJzsu0zsCGcm9t3OMuDCoI+oXQKZBX/iBFo/+TfOf0w8YMbeDZELHUBShN+Pw7It0rzsBY0/ScN9EFRAg9uFE5AKFzlEMdDnHdEk5IX8bn+q/iNyuZ20Zfatx/n1u1r/+IDKtrxUDyPpRxzeHlc0eIzN7tNfnW+BjWenmlK+/qzqb58s8VEJ8vwKjXnIuBRUnt8IXouCqR4pZjQ7VY/pasfJcb8wBvCMOJ8szpoZv2kB0UglhQ/f8VgMNVzSd7Jlbr/Gvikb4reFPoZuSQgCkL0wcT1txmPqF2hNd7Ct91Gp3CYwjGo1DSIl6UVedPYb9XRh2vhUkmn1L9ri4VtXvKxMZeda+gzcpzFkQtroQUHCtKvWYyDUzTIDT2uLvyusZLPrhI1t3r7Gqu4b7J9aGADUuCh4CmeoR5eIcOoagtiNcrd6H5HBnYuqHCw6mf4/9/+3E60SOkiTr2hhijwkaqif7r2jpwluHBZ2bxQ896Lnlh7WiwFZ6KxVL9TwKTUa0EvMW+xcT7Sh4hawjgEuZCzOjixNl1tcfqwBGxJDMHODTV2GBX7uHntPCGfiMbuuU/0CVn5VGuJETQXmOM7KT6kaK22TCJj7d3yw7dtROSJ2WIg7O5ieodfHYSrBlw3ZLnaLUx63K0BcJCrNUYJ8tO28x3swgVrNZHcQccsR/35rdrUuyd2E1CCdFW2/XnniY8ZJSrd33Xy+9dCjR5almlJwyLWAXHTIQJHMQjs5yhBNL9PJRYk9lxb3DulIAS37fHEzKiuNm8aA46SMz7EsDnTYdkPA7I+vf3GED6IiKeTjLD4z/LBN2FvUXxwmInaGAZJjZeWUa3cUSJCR+hyxRvAcUwWr9w1Yu+KHqXIAbMdrPYO+SCnK9nDBOVO/JL62Nb5gV5xpAIpeJW9dvABDPpEJyxtPTmL3J+5KPF0fNt678eLbYHJdp731j3JojVYvNx++eDJGg9LxLT88ECqFX1II+x2FiCSt2MG2JTR1OlWCtOMrpO1e5lzg0ScSU=";
        String key = null;
        try {
            key = DigestUtils.md5Hex("3c6e0b8a9c15224a8228b9a98ca1531d".getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        A=decryptData(A,key);
        System.out.println(A);

    }

}

