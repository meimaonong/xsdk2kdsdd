package com.art.app.orm.service.impl;

import com.art.app.orm.entity.UserInfo;
import com.art.app.orm.dao.UserInfoMapper;
import com.art.app.orm.service.UserInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author john
 * @since 2019-12-17
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
