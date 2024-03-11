package com.art.app.web.controller;

import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.web.annotation.LoginRequired;
import com.art.app.web.annotation.SignaturePass;
import com.art.app.web.bean.BizException;
import com.art.app.web.bean.request.RequestVo;
import com.art.app.web.bean.response.LoginResult;
import com.art.app.web.bean.response.ResponseVo;
import com.art.app.web.service.LoginService;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/sendSms")
    @SignaturePass
    public ResponseVo sendSms(@RequestBody RequestVo requestVo) {
        LOGGER.info("sendSms:{}", requestVo);
        Map<String, Object> params = requestVo.getBizParams();
        if (Objects.isNull(params) || Objects.isNull(params.get("phone"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        Map<String, Object> ret = Maps.newHashMap();
        ret.put("code", loginService.sendSms(String.valueOf(params.get("phone"))));
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(ret);
        return responseVo;
    }

    @PostMapping(value = "/login")
    @SignaturePass
    public ResponseVo login(@RequestBody RequestVo requestVo) {
        LOGGER.info("login:{}", requestVo);
        Map<String, Object> params = requestVo.getBizParams();
        if (Objects.isNull(params)
                || Objects.isNull(params.get("phone"))
                || Objects.isNull(params.get("verifyCode"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        String clientId = null;
        if (StringUtils.isNotBlank(requestVo.getBaseParams().getClientId())) {
            clientId = requestVo.getBaseParams().getClientId();
        }
        LoginResult loginResult = loginService.login(params, clientId);
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(loginResult);
        return responseVo;
    }

    @PostMapping(value = "/logout")
    @SignaturePass
    @LoginRequired
    public ResponseVo logout(@RequestBody RequestVo requestVo) {
        LOGGER.info("logout:{}", requestVo);
        loginService.logout(requestVo.getBaseParams().getToken());
        return new ResponseVo(ResponseCodeEnum.SUCCESS);
    }

    @GetMapping(value = "/login/valid")
    @SignaturePass
    public ResponseVo loginValid(@RequestParam("token") String token) {
        LOGGER.info("loginValid:{}", token);
        ResponseVo responseVo = new ResponseVo();
        responseVo.setData(loginService.validLogin(token));
        return responseVo;
    }

}
