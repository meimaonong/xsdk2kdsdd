package com.art.app.orm.service.impl;

import com.art.app.orm.entity.User;
import com.art.app.orm.dao.UserMapper;
import com.art.app.orm.service.UserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author john
 * @since 2019-12-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
