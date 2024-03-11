package com.art.app.orm.service.impl;

import com.art.app.orm.dao.UserPushMsgMapper;
import com.art.app.orm.entity.UserPushMsg;
import com.art.app.orm.service.UserPushMsgService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dragon123
 * @since 2020-05-30
 */
@Service
public class UserPushMsgServiceImpl extends ServiceImpl<UserPushMsgMapper, UserPushMsg> implements UserPushMsgService {

    @Resource
    private UserPushMsgMapper userPushMsgMapper;

    @Override
    public String findLatestMsgByUserIdAndType(int userId, int type) {
        return userPushMsgMapper.findLatestMsgByUserIdAndType(userId, type);
    }
}
