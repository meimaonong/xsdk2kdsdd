package com.art.app.web.service.impl;

import com.art.app.common.enums.MemberDurationUnitEnum;
import com.art.app.common.enums.OrderPaymentStatusEnum;
import com.art.app.common.enums.OrderStatusEnum;
import com.art.app.common.enums.OrderTypeEnum;
import com.art.app.common.pattern.entity.PaymentStatusParams;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.orm.entity.MemberDescriptionInfo;
import com.art.app.orm.entity.UserInfo;
import com.art.app.orm.entity.XslOrderInfoMember;
import com.art.app.orm.service.MemberDescriptionInfoService;
import com.art.app.orm.service.UserInfoService;
import com.art.app.orm.service.XslOrderInfoMemberService;
import com.art.app.web.service.AbstractPaymentStatusObserver;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;

@Service
@Slf4j
public class PaymentStatusMemberOrderObserverImpl extends AbstractPaymentStatusObserver {

    @Resource
    private XslOrderInfoMemberService xslOrderInfoMemberService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private MemberDescriptionInfoService memberDescriptionInfoService;

    @Override
    public boolean support(PaymentStatusParams params) {
        return OrderTypeEnum.MEMBER.getType() == params.getArtOrderType();
    }

    @Override
    protected void updateOrderStatus(PaymentStatusParams params, OrderStatusEnum orderStatusEnum, OrderPaymentStatusEnum orderPaymentStatusEnum) {
        Wrapper wrapper = Condition.create()
                .eq("order_id", params.getArtOrderId())
                .and().eq("del_flag", 0);
        XslOrderInfoMember memberOrderInfo = xslOrderInfoMemberService.selectOne(wrapper);
        if (null != memberOrderInfo) {
            XslOrderInfoMember updateInfo = new XslOrderInfoMember();
            updateInfo.setId(memberOrderInfo.getId());
            updateInfo.setOrderStatus(orderStatusEnum.getType());
            updateInfo.setPaymentStatus(orderPaymentStatusEnum.getStatus());
            updateInfo.setPaymentType(params.getPaymentType());
            updateInfo.setUpdatedAt(DatetimeUtils.now());
            xslOrderInfoMemberService.updateById(updateInfo);
            // 付款成功，更改会员等级
            Wrapper userInfoWrapper = Condition.create()
                    .eq("user_id", memberOrderInfo.getUserId())
                    .and().eq("del_flag", 0);
            UserInfo userInfo = userInfoService.selectOne(userInfoWrapper);
            UserInfo userUpdateInfo = new UserInfo();
            userUpdateInfo.setId(userInfo.getId());
            userUpdateInfo.setDegree(memberOrderInfo.getMemberLevel());
            Date expireTime = convertExpireTime(userInfo.getExpireTime(), memberOrderInfo);
            userUpdateInfo.setExpireTime(expireTime);
            userUpdateInfo.setUpdatedAt(DatetimeUtils.now());
            userInfoService.updateById(userUpdateInfo);
        }
    }

    private Date convertExpireTime(Date currentExpireTime, XslOrderInfoMember memberOrderInfo) {
        Wrapper memberWrapper = Condition.create()
                .eq("del_flag", 0)
                .and().eq("member_level", memberOrderInfo.getMemberLevel())
                .and().eq("member_level_version", memberOrderInfo.getMemberLevelVersion())
                .orderDesc(Collections.singleton("id"))
                .last("limit 1");
        MemberDescriptionInfo memberDescriptionInfo = memberDescriptionInfoService.selectOne(memberWrapper);
        return MemberDurationUnitEnum.convertDuration(currentExpireTime, memberDescriptionInfo.getDuration(), memberDescriptionInfo.getDurationUnit());
    }

}
