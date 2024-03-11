package com.art.app.web.task.impl;

import com.art.app.common.enums.OrderTypeEnum;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.orm.entity.XslOrderInfoClass;
import com.art.app.orm.service.XslOrderInfoClassService;
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
public class ClassOrderCancelTask extends AbstractOrderCancelTask {

    @Resource
    private XslOrderInfoClassService xslOrderInfoClassService;

    @Override
    protected void updateOrderInfo(Integer id, Integer orderStatus, Integer orderPaymentStatus) {
        XslOrderInfoClass updateInfo = new XslOrderInfoClass();
        updateInfo.setId(id);
        updateInfo.setOrderStatus(orderStatus);
        updateInfo.setPaymentStatus(orderPaymentStatus);
        updateInfo.setUpdatedAt(DatetimeUtils.now());
        xslOrderInfoClassService.updateById(updateInfo);
    }

    @Override
    protected List<OrderCancelBasicInfo> queryOrderCancelBasicInfo(List<Integer> statusEnums, Integer id, Integer limit, Date maxPaymentTime) {
        Wrapper<XslOrderInfoClass> listWrap = Condition.create()
                .eq("del_flag", 0)
                .and().lt("created_at", maxPaymentTime)
                .and().in("payment_status", statusEnums)
                .and().gt("id", id)
                .last("limit " + limit);
        List<XslOrderInfoClass> orderInfos = xslOrderInfoClassService.selectList(listWrap);
        return orderInfos.stream().map(xslOrderInfoClass -> {
            OrderCancelBasicInfo basicInfo = new OrderCancelBasicInfo();
            basicInfo.setId(xslOrderInfoClass.getId());
            basicInfo.setOrderType(OrderTypeEnum.CLASS.getType());
            basicInfo.setOrderId(xslOrderInfoClass.getOrderId());
            basicInfo.setOrderStatus(xslOrderInfoClass.getOrderStatus());
            basicInfo.setPaymentStatus(xslOrderInfoClass.getPaymentStatus());
            basicInfo.setCreatedAt(xslOrderInfoClass.getCreatedAt());
            return basicInfo;
        }).collect(Collectors.toList());
    }

    @Override
    protected Integer queryNumbersByStatus(List<Integer> statusEnums, Date maxPaymentTime) {
        Wrapper<XslOrderInfoClass> numberWrap = Condition.create()
                .eq("del_flag", 0)
                .and().lt("created_at", maxPaymentTime)
                .and().in("payment_status", statusEnums);
        return xslOrderInfoClassService.selectCount(numberWrap);
    }
}
