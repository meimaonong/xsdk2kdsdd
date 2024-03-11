package com.art.app.web.bean.response;

import com.art.app.common.Constants;
import lombok.Data;

@Data
public class LoginResult {

    private int userId;
    private int newUserFlag = Constants.NEW_USER_FLAG;
    private String token;
}
