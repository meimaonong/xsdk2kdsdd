package com.art.app.orm.service;

import com.art.app.orm.entity.UserPushToken;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dragon123
 * @since 2020-05-27
 */
public interface UserPushTokenService extends IService<UserPushToken> {

    int replace(UserPushToken r);
}
