package com.art.app.web.controller;

import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.web.annotation.LoginRequired;
import com.art.app.web.annotation.SignaturePass;
import com.art.app.web.bean.response.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/query")
    @LoginRequired
    @SignaturePass
    public ResponseVo query(
            @RequestParam(value = "keywords", required = false) String keywords) {
        LOGGER.info("query, keywords:{}", keywords);
        return new ResponseVo(ResponseCodeEnum.SUCCESS);
    }

}
