package com.art.app.orm.service.impl;

import com.art.app.orm.dao.UserBanRecordMapper;
import com.art.app.orm.entity.UserBanRecord;
import com.art.app.orm.service.UserBanRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dragon123
 * @since 2020-04-09
 */
@Service
public class UserBanRecordServiceImpl extends ServiceImpl<UserBanRecordMapper, UserBanRecord> implements UserBanRecordService {

    @Resource
    private UserBanRecordMapper userBanRecordMapper;

    @Override
    public int replace(UserBanRecord r) {
        return userBanRecordMapper.replace(r);
    }
}
