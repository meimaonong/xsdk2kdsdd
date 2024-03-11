package com.art.app.web.service.impl;

import com.art.app.common.Constants;
import com.art.app.common.enums.OrderPaymentStatusEnum;
import com.art.app.common.enums.OrderStatusEnum;
import com.art.app.common.enums.OrderTypeEnum;
import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.common.util.OrderUtils;
import com.art.app.orm.entity.MemberDescriptionInfo;
import com.art.app.orm.entity.XslOrderInfo;
import com.art.app.orm.entity.XslOrderInfoMember;
import com.art.app.orm.service.MemberDescriptionInfoService;
import com.art.app.orm.service.XslOrderInfoMemberService;
import com.art.app.web.bean.OrderException;
import com.art.app.web.bean.request.activity.ActivitySubListBizParam;
import com.art.app.web.bean.request.order.MemberOrderBizParam;
import com.art.app.web.bean.request.order.OrderRequestVo;
import com.art.app.web.bean.response.activity.ActivitySubDetail;
import com.art.app.web.bean.response.activity.ActivitySubList;
import com.art.app.web.bean.response.activity.CommonActivityInfo;
import com.art.app.web.bean.OrderBasicInfo;
import com.art.app.web.service.AbstractOrderInfoService;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MemberOrderInfoServiceImpl extends AbstractOrderInfoService {

    @Resource
    private MemberDescriptionInfoService memberDescriptionInfoService;
    @Resource
    private XslOrderInfoMemberService xslOrderInfoMemberService;

    @Override
    public boolean matching(Integer orderType) {
        return OrderTypeEnum.MEMBER.getType() == orderType;
    }

    @Override
    public CommonActivityInfo queryCommonActivityInfo(int userId) {
        Wrapper wrapper = Condition.create()
                .eq("user_id", userId)
                .and().eq("del_flag", 0);
        int count = xslOrderInfoMemberService.selectCount(wrapper);
        CommonActivityInfo info = new CommonActivityInfo();
        info.setOrderNumbers(count);
        return info;
    }

    @Override
    public void updateOrderPaymentStatus(XslOrderInfo xslOrderInfo, OrderPaymentStatusEnum fromStatus, OrderPaymentStatusEnum toStatus) {
        Wrapper wrapper = Condition.create()
                .eq("order_id", xslOrderInfo.getOrderId())
                .and().eq("del_flag", 0)
                .orderDesc(Collections.singleton("id"))
                .last("limit 1");
        XslOrderInfoMember orderInfo = xslOrderInfoMemberService.selectOne(wrapper);
        if (fromStatus.getStatus().equals(orderInfo.getOrderStatus())) {
            XslOrderInfoMember updateInfo = new XslOrderInfoMember();
            updateInfo.setId(orderInfo.getId());
            updateInfo.setPaymentStatus(toStatus.getStatus());
            updateInfo.setUpdatedAt(DatetimeUtils.now());
            xslOrderInfoMemberService.updateById(updateInfo);
        }
    }

    @Override
    public Integer countActivitySubList(ActivitySubListBizParam bizParams, int userId) {
        return 0;
    }

    @Override
    public List<ActivitySubList> queryActivitySubList(ActivitySubListBizParam bizParams, int start, int userId) {
        return new ArrayList<>();
    }

    @Override
    public ActivitySubDetail queryActivitySubDetail(String orderId) {
        return null;
    }

    @Override
    public OrderBasicInfo queryOrderBasicInfo(String orderId) {
        Wrapper wrapper = Condition.create()
                .eq("order_id", orderId)
                .and().eq("del_flag", 0)
                .orderDesc(Collections.singleton("id"))
                .last("limit 1");
        XslOrderInfoMember orderInfo = xslOrderInfoMemberService.selectOne(wrapper);
        OrderBasicInfo result = new OrderBasicInfo();
        result.setOrderId(orderId);
        result.setOrderType(OrderTypeEnum.MEMBER.getType());
        result.setOrderStatus(orderInfo.getOrderStatus());
        result.setPaymentStatus(orderInfo.getPaymentStatus());
        MemberDescriptionInfo descriptionInfo = queryMemberDescriptionInfo(orderInfo.getMemberLevel(), orderInfo.getMemberLevelVersion());
        result.setSubject(descriptionInfo.getMemberLevelName());
        result.setTimeExpire(DatetimeUtils.addMinute(DatetimeUtils.now(), Constants.MAX_PAYMENT_TIME));
        result.setCreatedAt(orderInfo.getCreatedAt());
        return result;
    }

    @Override
    protected XslOrderInfo buildXslOrderInfo(OrderRequestVo requestVo, int userId) {
        MemberOrderBizParam orderBizParams = (MemberOrderBizParam) requestVo.getOrderBizParams(MemberOrderBizParam.class);
        MemberDescriptionInfo memberDescriptionInfo = queryLatestMemberDescriptionInfo(orderBizParams.getMemberLevel());
        if (Objects.nonNull(memberDescriptionInfo)) {
            if (null != memberDescriptionInfo.getPromotion() && BigDecimal.ZERO.compareTo(memberDescriptionInfo.getPromotion()) <= 0) {
                // 价格必须大于等于0
                XslOrderInfo xslOrderInfo = new XslOrderInfo();
                xslOrderInfo.setUserId(userId);
                xslOrderInfo.setOrderId(OrderUtils.createOrderId("MEM"));
                xslOrderInfo.setOrderType(OrderTypeEnum.MEMBER.getType());
                xslOrderInfo.setRemark("");
                xslOrderInfo.setAmount(memberDescriptionInfo.getPromotion());
                xslOrderInfo.setCreatedAt(DatetimeUtils.now());
                xslOrderInfo.setUpdatedAt(xslOrderInfo.getCreatedAt());
                xslOrderInfo.setDelFlag(0);
                return xslOrderInfo;
            } else {
                throw new OrderException(ResponseCodeEnum.MEMBER_DESCRIPTION_APPLY_PRICE_ERROR);
            }
        } else {
            throw new OrderException(ResponseCodeEnum.MEMBER_DESCRIPTION_INVALID_ERROR);
        }
    }

    @Override
    protected void saveOrderDetailInfo(OrderRequestVo requestVo, int userId, String orderId) {
        MemberOrderBizParam orderBizParams = (MemberOrderBizParam) requestVo.getOrderBizParams(MemberOrderBizParam.class);
        Integer memberLevel = orderBizParams.getMemberLevel();
        MemberDescriptionInfo descInfo = queryLatestMemberDescriptionInfo(memberLevel);
        XslOrderInfoMember memberOrderInfo = new XslOrderInfoMember();
        memberOrderInfo.setUserId(userId);
        memberOrderInfo.setOrderId(orderId);
        memberOrderInfo.setOrderStatus(OrderStatusEnum.APPLYING.getType());
        memberOrderInfo.setPaymentStatus(OrderPaymentStatusEnum.NOT_PAID.getStatus());
        memberOrderInfo.setMemberLevel(memberLevel);
        memberOrderInfo.setMemberLevelVersion(descInfo.getMemberLevelVersion());
        memberOrderInfo.setAmount(descInfo.getPrice());
        memberOrderInfo.setPayAmount(descInfo.getPromotion());
        memberOrderInfo.setRemark(orderBizParams.getRemark());
        memberOrderInfo.setCreatedAt(DatetimeUtils.now());
        memberOrderInfo.setUpdatedAt(memberOrderInfo.getCreatedAt());
        memberOrderInfo.setDelFlag(0);
        xslOrderInfoMemberService.insert(memberOrderInfo);

    }

    private MemberDescriptionInfo queryLatestMemberDescriptionInfo(Integer memberLevel) {
        Wrapper wrapper = Condition.create()
                .eq("del_flag", 0)
                .and().eq("member_level", memberLevel)
                .orderDesc(Collections.singleton("id"))
                .last("limit 1");
        MemberDescriptionInfo memberDescriptionInfo = memberDescriptionInfoService.selectOne(wrapper);
        return memberDescriptionInfo;
    }

    private MemberDescriptionInfo queryMemberDescriptionInfo(Integer memberLevel, Integer memberLevelVersion) {
        Wrapper wrapper = Condition.create()
                .eq("del_flag", 0)
                .and().eq("member_level", memberLevel)
                .and().eq("member_level_version", memberLevelVersion)
                .orderDesc(Collections.singleton("id"))
                .last("limit 1");
        MemberDescriptionInfo memberDescriptionInfo = memberDescriptionInfoService.selectOne(wrapper);
        return memberDescriptionInfo;
    }
}
