package com.art.app.web.controller;

import com.art.app.common.enums.OssStorageTypeEnum;
import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.mw.aliyun.OssManager;
import com.art.app.web.annotation.LoginRequired;
import com.art.app.web.bean.BizException;
import com.art.app.web.bean.request.RequestVo;
import com.art.app.web.bean.response.ResponseVo;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/oss")
public class OssSignController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OssSignController.class);

    @Autowired
    private OssManager ossManager;

    @PostMapping(value = "/sign")
    @LoginRequired
    public ResponseVo getOssSign(@RequestBody RequestVo requestVo) {
        LOGGER.info("getOssSign:{}", requestVo);
        Map<String, Object> params = requestVo.getBizParams();
        if (MapUtils.isEmpty(params) || Objects.isNull(params.get("type"))) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        try {
            responseVo.setData(ossManager.getSign(
                    OssStorageTypeEnum.ofType(Integer.parseInt(String.valueOf(params.get("type")))).getDir()));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("getOssSign for {}", params.get("type"), e);
            throw new BizException(ResponseCodeEnum.SYSTEM_ERROR);
        }
        return responseVo;
    }
}
