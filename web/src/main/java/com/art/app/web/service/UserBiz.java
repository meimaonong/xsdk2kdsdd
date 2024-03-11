package com.art.app.web.service;

import com.art.app.common.enums.CouponStatusEnum;
import com.art.app.common.enums.CouponTypeEnum;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.orm.entity.Coupon;
import com.art.app.orm.entity.InviteAssociation;
import com.art.app.orm.entity.UserCoupon;
import com.art.app.orm.service.CouponService;
import com.art.app.orm.service.InviteAssociationService;
import com.art.app.orm.service.UserCouponService;
import com.baomidou.mybatisplus.mapper.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class UserBiz {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBiz.class);

    @Autowired
    private CouponService couponService;
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private InviteAssociationService inviteAssociationService;

    @SuppressWarnings("all")
    public void processInvition(String phone, int userId) {
        InviteAssociation inviteAssociation = inviteAssociationService.selectOne(Condition.create()
                .eq("del_flag", 0).and().eq("invite_tel", phone));
        if (Objects.nonNull(inviteAssociation)) {
            InviteAssociation updateInvite = new InviteAssociation();
            updateInvite.setInviteId(userId);
            updateInvite.setUpdatedAt(DatetimeUtils.now());
            inviteAssociationService.update(updateInvite, Condition.create()
                    .eq("del_flag", 0).and().eq("id", inviteAssociation.getId()));
        }
    }

    @SuppressWarnings("all")
    public void processCoupon(int userId) {
        Coupon coupon = new Coupon();
        coupon.setAmount(BigDecimal.valueOf(500));
        coupon.setCreatedAt(DatetimeUtils.now());
        coupon.setDelFlag(0);
        coupon.setDispatched(1);
        coupon.setStock(1);
        coupon.setType(CouponTypeEnum.VOUCHER.getType());
        coupon.setDetail(CouponTypeEnum.VOUCHER.getDesc());
        coupon.setValidStartAt(DatetimeUtils.now());
        coupon.setValidEndAt(DatetimeUtils.addDay(coupon.getValidStartAt(), 365));
        couponService.insert(coupon);
        int couponId = coupon.getId();
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setCouponId(couponId);
        userCoupon.setCreatedAt(DatetimeUtils.now());
        userCoupon.setDelFlag(0);
        userCoupon.setStatus(CouponStatusEnum.NOT_USED.getType());
        userCoupon.setType(CouponTypeEnum.VOUCHER.getType());
        userCoupon.setUserId(userId);
        userCouponService.insert(userCoupon);
    }
}
