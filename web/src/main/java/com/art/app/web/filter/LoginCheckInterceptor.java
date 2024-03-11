package com.art.app.web.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.art.app.common.Constants;
import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.common.util.Base64Utils;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.orm.entity.AccessLog;
import com.art.app.orm.entity.User;
import com.art.app.orm.entity.UserToken;
import com.art.app.orm.service.UserService;
import com.art.app.orm.service.UserTokenService;
import com.art.app.web.annotation.LoginRequired;
import com.art.app.web.annotation.SignaturePass;
import com.art.app.web.bean.BizException;
import com.art.app.web.bean.MyRequestWrapper;
import com.art.app.web.bean.request.RequestVo;
import com.art.app.web.service.AccessService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.baomidou.mybatisplus.mapper.Condition;
import com.google.common.collect.Lists;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Component
public class LoginCheckInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginCheckInterceptor.class);

    private ApplicationContext context;

    private List<String> SKIP_CHECK_URLS = Lists.newArrayList("/login/valid");

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.context = applicationContext;
    }

    @SuppressWarnings("all")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            if (request instanceof MyRequestWrapper) {
                if (SKIP_CHECK_URLS.contains(request.getRequestURI())) {
                    return true;
                }
                HandlerMethod method = (HandlerMethod) handler;
                MyRequestWrapper requestWrapper = (MyRequestWrapper) request;
                String requestContent = getRequestContent(requestWrapper);
                LOGGER.info("url -> {}, request -> {}", requestWrapper.getRequestURL().toString(), requestContent);
                RequestVo requestVo = JSON.parseObject(requestContent, RequestVo.class);

                AccessService accessService = (AccessService) context.getBean("accessService");
                AccessLog accessLog = new AccessLog();
                accessLog.setUserId(0);
                accessLog.setDelFlag(0);
                accessLog.setCreatedAt(DatetimeUtils.now());
                accessLog.setPath(request.getRequestURI());
                if (Objects.isNull(requestVo) || Objects.isNull(requestVo.getBaseParams())) {
                    accessService.recordAccessLog(accessLog);
                    throw new BizException(ResponseCodeEnum.PARAM_ERROR);
                }

                SignaturePass signaturePass = method.getMethodAnnotation(SignaturePass.class);
                if (Objects.isNull(signaturePass) || !signaturePass.pass()) {
                    // 验签
                    try {
                        validateSignature(requestVo);
                    } catch (Exception e) {
                        accessService.recordAccessLog(accessLog);
                        throw e;
                    }
                }

                LoginRequired loginRequired = method.getMethodAnnotation(LoginRequired.class);
                if (Objects.nonNull(loginRequired) && loginRequired.required()) {
                    try {
                        UserService userService = (UserService) context.getBean("userServiceImpl");
                        UserTokenService userTokenService = (UserTokenService) context.getBean("userTokenServiceImpl");
                        String token = requestVo.getBaseParams().getToken();
                        String userId = JWT.decode(token).getAudience().get(0);
                        if (StringUtils.isBlank(userId)) {
                            accessService.recordAccessLog(accessLog);
                            throw new BizException(ResponseCodeEnum.INVALID_USER);
                        }
                        User user = userService.selectOne(Condition.create().eq("id", Integer.parseInt(userId)));
                        if (Objects.isNull(user)) {
                            accessService.recordAccessLog(accessLog);
                            throw new BizException(ResponseCodeEnum.INVALID_USER);
                        }
                        // 验证 token
                        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(Constants.JWT_SECRET)).build();
                        try {
                            jwtVerifier.verify(token);
                        } catch (JWTVerificationException e) {
                            accessService.recordAccessLog(accessLog);
                            throw new BizException(ResponseCodeEnum.INVALID_TOKEN);
                        }
                        UserToken userToken = userTokenService.selectOne(
                                Condition.create().eq("token", token).and().eq("del_flag", 0));
                        if (Objects.isNull(userToken)) {
                            accessService.recordAccessLog(accessLog);
                            throw new BizException(ResponseCodeEnum.INVALID_USER);
                        }
                        accessLog.setUserId(userToken.getUserId());
                        accessService.recordAccessLog(accessLog);
                    } catch (JWTDecodeException e) {
                        LOGGER.error("decode JWT error", e);
                        accessService.recordAccessLog(accessLog);
                        throw new BizException(ResponseCodeEnum.INVALID_TOKEN);
                    }
                } else {
                    accessService.recordAccessLog(accessLog);
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

    public static String getRequestContent(MyRequestWrapper request) {
        String content = "";
        if (request.getMethod().equalsIgnoreCase("GET")) {
            String queryString = request.getQueryString();
            String[] parts = queryString.split("=");
            if (parts.length == 2 && "data".equalsIgnoreCase(parts[0])) {
                content = Base64Utils.decodeParams(parts[1]);
            }
        } else if (request.getMethod().equalsIgnoreCase("POST")) {
            content = request.getBody();
        }
        return content;
    }

    private void validateSignature(RequestVo requestVo) {
        long timestamp = requestVo.getBaseParams().getTimestamp();
        if (timestamp <= 0) {
            throw new BizException(ResponseCodeEnum.TIMESTAMP_MISSING);
        }
        String signature = requestVo.getSignature();
        if (StringUtils.isBlank(signature) || Objects.isNull(requestVo.getBizParams())) {
            throw new BizException(ResponseCodeEnum.SIGNATURE_MISSING);
        }
        String compareSignature = DigestUtils.md5Hex(
                JSON.toJSONString(requestVo.getBizParams(), SerializerFeature.MapSortField) + "pqwo!@#$");
        if (!signature.equals(compareSignature)) {
            LOGGER.error("validateSignature inconsistent: calculated:[{}], requestVo:[{}]"
                    , compareSignature, JSON.toJSONString(requestVo));
            throw new BizException(ResponseCodeEnum.AUTH_FAIL);
        }
    }

}
