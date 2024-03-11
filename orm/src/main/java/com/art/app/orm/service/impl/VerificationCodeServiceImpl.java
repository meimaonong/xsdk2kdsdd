package com.art.app.orm.service.impl;

import com.art.app.orm.entity.VerificationCode;
import com.art.app.orm.dao.VerificationCodeMapper;
import com.art.app.orm.service.VerificationCodeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 验证码表 服务实现类
 * </p>
 *
 * @author john
 * @since 2019-12-19
 */
@Service
public class VerificationCodeServiceImpl extends ServiceImpl<VerificationCodeMapper, VerificationCode> implements VerificationCodeService {

}
