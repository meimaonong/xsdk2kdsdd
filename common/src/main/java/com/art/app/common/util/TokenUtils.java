package com.art.app.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.art.app.common.Constants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.collect.Maps;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

public class TokenUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(TokenUtils.class);

    public static String buildToken(Integer userId) {
        String token;
        token = JWT.create().withAudience(userId.toString())
                .withExpiresAt(DateUtils.addDays(DatetimeUtils.now(), 15))
                .sign(Algorithm.HMAC256(Constants.JWT_SECRET));
        return token;
    }

    public static int getUserIdByToken(String token) {
        if (StringUtils.isBlank(token)) {
            return 0;
        }
        return Integer.parseInt(JWT.decode(token).getAudience().get(0));
    }

    public static void main(String[] args) {
//        String text = "{\"blocks\":[{\"text\":\"我是正文一\",\"type\":1},{\"text\":\"我是正文二\",\"type\":1},{\"imgDesc\":\"我是图一\",\"imgUrl\":\"https://meimaonong.oss-cn-shanghai.aliyuncs.com/icon/wx_camera_1558092650900.jpg\",\"type\":0},{\"imgDesc\":\"我是图二\",\"imgUrl\":\"https://meimaonong.oss-cn-shanghai.aliyuncs.com/icon/wx_camera_1558008332636.jpg\",\"type\":0}],\"description\":\"我是副标题\",\"draft\":false,\"mode\":0,\"resourceId\":\"\",\"title\":\"我是标题\",\"type\":0}";
//        Map<String, Object> bizParams = JSON.parseObject(text, new TypeReference<Map<String, Object>>() {
//        });
//        System.out.println(JSON.toJSONString(bizParams, SerializerFeature.MapSortField));

        System.out.println(System.currentTimeMillis());
        Map<String, Object> bizParams = Maps.newHashMap();
        bizParams.put("phone", 13914709325L);
        System.out.println(DigestUtils.md5Hex(JSON.toJSONString(bizParams,
                SerializerFeature.MapSortField) + "pqwo!@#$"));
//        String token = buildToken(111);
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(Constants.JWT_SECRET)).build();
//        try {
//            Thread.sleep(2000);
//            jwtVerifier.verify(token);
//        } catch (Exception e) {
//            LOGGER.error("test error", e);
//        }
    }
}
