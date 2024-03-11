package com.art.app.orm.service.impl;

import com.art.app.orm.entity.LoginActiveRecord;
import com.art.app.orm.dao.LoginActiveRecordMapper;
import com.art.app.orm.service.LoginActiveRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录或激活记录表 服务实现类
 * </p>
 *
 * @author dragon123
 * @since 2019-12-17
 */
@Service
public class LoginActiveRecordServiceImpl extends ServiceImpl<LoginActiveRecordMapper, LoginActiveRecord> implements LoginActiveRecordService {

}
