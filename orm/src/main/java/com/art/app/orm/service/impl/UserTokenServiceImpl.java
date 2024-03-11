package com.art.app.orm.service.impl;

import com.art.app.orm.entity.UserToken;
import com.art.app.orm.dao.UserTokenMapper;
import com.art.app.orm.service.UserTokenService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author john
 * @since 2019-12-17
 */
@Service
public class UserTokenServiceImpl extends ServiceImpl<UserTokenMapper, UserToken> implements UserTokenService {

}
