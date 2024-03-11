package com.art.app.orm.service.impl;

import com.art.app.orm.dao.UserPushTokenMapper;
import com.art.app.orm.entity.UserPushToken;
import com.art.app.orm.service.UserPushTokenService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dragon123
 * @since 2020-05-27
 */
@Service
public class UserPushTokenServiceImpl extends ServiceImpl<UserPushTokenMapper, UserPushToken> implements UserPushTokenService {

    @Resource
    private UserPushTokenMapper userPushTokenMapper;

    @Override
    public int replace(UserPushToken r) {
        return userPushTokenMapper.replace(r);
    }
}
