package com.art.app.web.bean.response;

import com.art.app.common.enums.ResponseCodeEnum;
import lombok.Data;

@Data
public class ResponseVo {
    private int code;
    private String message;
    private Object data;

    public ResponseVo(ResponseCodeEnum responseCodeEnum) {
        this.code = responseCodeEnum.getCode();
        this.message = responseCodeEnum.getMsg();
    }

    public ResponseVo() {
    }

    public static ResponseVo successData(Object data) {
        ResponseVo responseVo = new ResponseVo(ResponseCodeEnum.SUCCESS);
        responseVo.setData(data);
        return responseVo;
    }
}