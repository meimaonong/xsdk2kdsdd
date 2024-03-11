package com.art.app.orm.service.impl;

import com.art.app.orm.entity.UserCoupon;
import com.art.app.orm.dao.UserCouponMapper;
import com.art.app.orm.service.UserCouponService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户名下优惠券信息表 服务实现类
 * </p>
 *
 * @author john
 * @since 2020-06-27
 */
@Service
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {

}
