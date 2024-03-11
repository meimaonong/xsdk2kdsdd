package com.art.app.orm.service.impl;

import com.art.app.orm.entity.UserAuth;
import com.art.app.orm.dao.UserAuthMapper;
import com.art.app.orm.service.UserAuthService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dragon123
 * @since 2020-05-30
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {

}
