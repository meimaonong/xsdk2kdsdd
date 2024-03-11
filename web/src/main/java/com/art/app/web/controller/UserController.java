package com.art.app.web.controller;

import com.alibaba.fastjson.JSON;
import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.common.util.Base64Utils;
import com.art.app.web.annotation.LoginRequired;
import com.art.app.web.annotation.SignaturePass;
import com.art.app.web.bean.BizException;
import com.art.app.web.bean.request.RequestVo;
import com.art.app.web.bean.response.ResponseVo;
import com.art.app.web.service.ContentService;
import com.art.app.web.service.LoginService;
import org.apache.commons.collections.MapUtils;
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
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private LoginService loginService;
    @Autowired
    private ContentService contentService;

    @PostMapping(value = "/edit/profile")
    @SignaturePass
    @LoginRequired
    public ResponseVo editProfile(@RequestBody RequestVo requestVo) {
        LOGGER.info("editProfile:{}", requestVo);
        Map<String, Object> params = requestVo.getBizParams();
        if (Objects.isNull(params)
                || (Objects.isNull(params.get("nickName"))
                && Objects.isNull(params.get("avatar"))
                && Objects.isNull(params.get("bgImgUrl"))
                && Objects.isNull(params.get("motto")))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        loginService.editProfile(requestVo.getBaseParams().getToken(), params);
        return new ResponseVo(ResponseCodeEnum.SUCCESS);
    }

    @GetMapping(value = "/repair")
    @SignaturePass
    public ResponseVo repair() {
        contentService.repair();
        return new ResponseVo(ResponseCodeEnum.SUCCESS);
    }

    @PostMapping(value = "/focus")
    @SignaturePass
    @LoginRequired
    public ResponseVo focus(@RequestBody RequestVo requestVo) {
        LOGGER.info("focus:{}", requestVo);
        Map<String, Object> params = requestVo.getBizParams();
        if (Objects.isNull(params)
                || Objects.isNull(params.get("focusAt"))
                || Objects.isNull(params.get("action"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        loginService.focusAction(requestVo.getBaseParams().getToken()
                , Integer.parseInt(String.valueOf(params.get("focusAt")))
                , Integer.parseInt(String.valueOf(params.get("action"))));
        return new ResponseVo(ResponseCodeEnum.SUCCESS);
    }

    /**
     * 申请成为艺术家
     *
     * @param requestVo
     * @return
     */
    @PostMapping(value = "/apply/artist")
    @SignaturePass
    @LoginRequired
    public ResponseVo applyArtist(@RequestBody RequestVo requestVo) {
        LOGGER.info("applyArtist:{}", requestVo);
        loginService.applyArtist(requestVo.getBaseParams().getToken());
        return new ResponseVo(ResponseCodeEnum.SUCCESS);
    }

    /**
     * 个人主页概览接口
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/personalPage")
    @SignaturePass
    @SuppressWarnings("all")
    public ResponseVo personalPage(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("personalPage:{}", requestVo);
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.personalPage(requestVo.getBizParams(), requestVo.getBaseParams().getToken()));
        return responseVo;
    }

    /**
     * 个人主页概览接口
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/personalPage/articles")
    @SignaturePass
    @SuppressWarnings("all")
    public ResponseVo personalPageArticles(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("personalPageArticles:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.personalPageArticles(
                requestVo.getBizParams(), requestVo.getBaseParams().getToken()));
        return responseVo;
    }

    /**
     * 画友圈
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/personal/relation")
    @SignaturePass
    @SuppressWarnings("all")
    public ResponseVo personalRelation(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("personalRelation:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("type"))
                || Objects.isNull(requestVo.getBizParams().get("pageIndex"))
                || Objects.isNull(requestVo.getBizParams().get("pageSize"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.personalRelation(requestVo.getBizParams(), requestVo.getBaseParams().getToken()));
        return responseVo;
    }

    /**
     * 我的屏蔽
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/blacklist")
    @SignaturePass
    @LoginRequired
    @SuppressWarnings("all")
    public ResponseVo blacklist(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("blacklist:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("pageIndex"))
                || Objects.isNull(requestVo.getBizParams().get("pageSize"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.blacklist(requestVo.getBizParams(), requestVo.getBaseParams().getToken()));
        return responseVo;
    }

    /**
     * 我的贡献值首页
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/points/home")
    @SignaturePass
    @LoginRequired
    @SuppressWarnings("all")
    public ResponseVo pointsHome(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("pointsHome:{}", requestVo);
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.pointsHome(requestVo.getBizParams(), requestVo.getBaseParams().getToken()));
        return responseVo;
    }

    /**
     * 贡献值排行榜
     *
     * @param data
     * @return
     */
    @GetMapping(value = "/points/contribution/list")
    @SignaturePass
    @LoginRequired
    @SuppressWarnings("all")
    public ResponseVo pointsContribution(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("pointsContribution:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("pageIndex"))
                || Objects.isNull(requestVo.getBizParams().get("pageSize"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.pointsContribution(requestVo.getBizParams()));
        return responseVo;
    }

    @GetMapping(value = "/points/history")
    @SignaturePass
    @LoginRequired
    @SuppressWarnings("all")
    public ResponseVo pointsHistory(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("pointsHistory:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("pageIndex"))
                || Objects.isNull(requestVo.getBizParams().get("pageSize"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.pointsHistory(requestVo.getBizParams(), requestVo.getBaseParams().getToken()));
        return responseVo;
    }

    @GetMapping(value = "/membership/home")
    @SignaturePass
    @LoginRequired
    public ResponseVo memberHome(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("memberHome:{}", requestVo);
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.memberHome(requestVo.getBaseParams().getToken()));
        return responseVo;
    }

    @PostMapping(value = "/membership/certificate")
    @SignaturePass
    @LoginRequired
    public ResponseVo memberCertificate(@RequestBody RequestVo requestVo) {
        LOGGER.info("memberCertificate:{}", requestVo);
        Map<String, Object> params = requestVo.getBizParams();
        if (Objects.isNull(params)
                || (Objects.isNull(params.get("name"))
                || Objects.isNull(params.get("type"))
                || Objects.isNull(params.get("gender"))
                || Objects.isNull(params.get("number"))
                || Objects.isNull(params.get("backImage"))
                || Objects.isNull(params.get("frontImage")))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        contentService.memberCertificate(params, requestVo.getBaseParams().getToken());
        return responseVo;
    }

    @GetMapping(value = "/membership/certificate/result")
    @SignaturePass
    @LoginRequired
    public ResponseVo memberCertificateResult(@RequestParam(value = "data") String data) {
        LOGGER.info("memberCertificateResult");
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("memberCertificateResult:{}", requestVo);
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.memberCertificateResult(requestVo.getBaseParams().getToken()));
        return responseVo;
    }

    @GetMapping(value = "/message/home")
    @SignaturePass
    @LoginRequired
    public ResponseVo messageHome(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("messageHome:{}", requestVo);
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.messageHome(requestVo.getBaseParams().getToken()));
        return responseVo;
    }

    @GetMapping(value = "/message/category")
    @SignaturePass
    @LoginRequired
    public ResponseVo messageCategory(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("messageHome:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("type"))
                || Objects.isNull(requestVo.getBizParams().get("pageIndex"))
                || Objects.isNull(requestVo.getBizParams().get("pageSize"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.messageCategory(requestVo.getBizParams(), requestVo.getBaseParams().getToken()));
        return responseVo;
    }

    @PostMapping(value = "/message/clean")
    @SignaturePass
    @LoginRequired
    public ResponseVo messageClean(@RequestBody RequestVo requestVo) {
        LOGGER.info("messageClean:{}", requestVo);
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        contentService.messageClean(requestVo.getBaseParams().getToken());
        return responseVo;
    }

    @PostMapping(value = "/message/read")
    @SignaturePass
    @LoginRequired
    public ResponseVo messageRead(@RequestBody RequestVo requestVo) {
        LOGGER.info("messageRead:{}", requestVo);
        Map<String, Object> params = requestVo.getBizParams();
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        contentService.messageRead(requestVo.getBaseParams().getToken(), params);
        return responseVo;
    }

    @PostMapping(value = "/message/push")
    @SignaturePass
    public ResponseVo messagePush(@RequestBody RequestVo requestVo) {
        LOGGER.info("messagePush:{}", requestVo);
        Map<String, Object> params = requestVo.getBizParams();
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        contentService.messagePush(params);
        return responseVo;
    }

    @GetMapping(value = "/coupons")
    @SignaturePass
    @LoginRequired
    public ResponseVo coupons(@RequestParam(value = "data") String data) {
        RequestVo requestVo = JSON.parseObject(Base64Utils.decodeParams(data), RequestVo.class);
        LOGGER.info("coupons:{}", requestVo);
        if (MapUtils.isEmpty(requestVo.getBizParams())
                || Objects.isNull(requestVo.getBizParams().get("type"))
                || Objects.isNull(requestVo.getBizParams().get("pageIndex"))
                || Objects.isNull(requestVo.getBizParams().get("pageSize"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(contentService.coupons(requestVo.getBizParams(), requestVo.getBaseParams().getToken()));
        return responseVo;
    }
}
