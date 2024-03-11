package com.art.app.web.task.impl;

import com.art.app.common.enums.OrderTypeEnum;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.orm.entity.XslOrderInfoMember;
import com.art.app.orm.service.XslOrderInfoMemberService;
import com.art.app.web.bean.OrderCancelBasicInfo;
import com.art.app.web.task.AbstractOrderCancelTask;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberOrderCancelTask extends AbstractOrderCancelTask {

    @Resource
    private XslOrderInfoMemberService xslOrderInfoMemberService;

    @Override
    protected void updateOrderInfo(Integer id, Integer orderStatus, Integer orderPaymentStatus) {
        XslOrderInfoMember updateInfo = new XslOrderInfoMember();
        updateInfo.setId(id);
        updateInfo.setOrderStatus(orderStatus);
        updateInfo.setPaymentStatus(orderPaymentStatus);
        updateInfo.setUpdatedAt(DatetimeUtils.now());
        xslOrderInfoMemberService.updateById(updateInfo);
    }

    @Override
    protected List<OrderCancelBasicInfo> queryOrderCancelBasicInfo(List<Integer> statusEnums, Integer id, Integer limit, Date maxPaymentTime) {
        Wrapper<XslOrderInfoMember> listWrap = Condition.create()
                .eq("del_flag", 0)
                .and().lt("created_at", maxPaymentTime)
                .and().in("payment_status", statusEnums)
                .and().gt("id", id)
                .last("limit " + limit);
        List<XslOrderInfoMember> orderInfos = xslOrderInfoMemberService.selectList(listWrap);
        return orderInfos.stream().map(orderInfo -> {
            OrderCancelBasicInfo basicInfo = new OrderCancelBasicInfo();
            basicInfo.setId(orderInfo.getId());
            basicInfo.setOrderType(OrderTypeEnum.MEMBER.getType());
            basicInfo.setOrderId(orderInfo.getOrderId());
            basicInfo.setOrderStatus(orderInfo.getOrderStatus());
            basicInfo.setPaymentStatus(orderInfo.getPaymentStatus());
            basicInfo.setCreatedAt(orderInfo.getCreatedAt());
            return basicInfo;
        }).collect(Collectors.toList());
    }

    @Override
    protected Integer queryNumbersByStatus(List<Integer> statusEnums, Date maxPaymentTime) {
        Wrapper<XslOrderInfoMember> numberWrap = Condition.create()
                .eq("del_flag", 0)
                .and().lt("created_at", maxPaymentTime)
                .and().in("payment_status", statusEnums);
        return xslOrderInfoMemberService.selectCount(numberWrap);
    }
}
