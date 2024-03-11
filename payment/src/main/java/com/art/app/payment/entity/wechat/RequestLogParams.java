package com.art.app.payment.entity.wechat;

import lombok.Data;

@Data
public class RequestLogParams {

    private String requestParam;
    private String responseParam;
    private boolean responseError;

}
