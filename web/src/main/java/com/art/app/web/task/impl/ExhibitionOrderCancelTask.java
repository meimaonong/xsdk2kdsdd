package com.art.app.web.task.impl;

import com.art.app.common.enums.OrderTypeEnum;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.orm.entity.XslOrderInfoExhibition;
import com.art.app.orm.service.XslOrderInfoExhibitionService;
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
public class ExhibitionOrderCancelTask extends AbstractOrderCancelTask {

    @Resource
    private XslOrderInfoExhibitionService xslOrderInfoExhibitionService;

    @Override
    protected void updateOrderInfo(Integer id, Integer orderStatus, Integer orderPaymentStatus) {
        XslOrderInfoExhibition updateInfo = new XslOrderInfoExhibition();
        updateInfo.setId(id);
        updateInfo.setOrderStatus(orderStatus);
        updateInfo.setPaymentStatus(orderPaymentStatus);
        updateInfo.setUpdatedAt(DatetimeUtils.now());
        xslOrderInfoExhibitionService.updateById(updateInfo);
    }

    @Override
    protected List<OrderCancelBasicInfo> queryOrderCancelBasicInfo(List<Integer> statusEnums, Integer id, Integer limit, Date maxPaymentTime) {
        Wrapper<XslOrderInfoExhibition> listWrap = Condition.create()
                .eq("del_flag", 0)
                .and().lt("created_at", maxPaymentTime)
                .and().in("payment_status", statusEnums)
                .and().gt("id", id)
                .last("limit " + limit);
        List<XslOrderInfoExhibition> orderInfos = xslOrderInfoExhibitionService.selectList(listWrap);
        return orderInfos.stream().map(orderInfo -> {
            OrderCancelBasicInfo basicInfo = new OrderCancelBasicInfo();
            basicInfo.setId(orderInfo.getId());
            basicInfo.setOrderType(OrderTypeEnum.EXHIBITION.getType());
            basicInfo.setOrderId(orderInfo.getOrderId());
            basicInfo.setOrderStatus(orderInfo.getOrderStatus());
            basicInfo.setPaymentStatus(orderInfo.getPaymentStatus());
            basicInfo.setCreatedAt(orderInfo.getCreatedAt());
            return basicInfo;
        }).collect(Collectors.toList());
    }

    @Override
    protected Integer queryNumbersByStatus(List<Integer> statusEnums, Date maxPaymentTime) {
        Wrapper<XslOrderInfoExhibition> numberWrap = Condition.create()
                .eq("del_flag", 0)
                .and().lt("created_at", maxPaymentTime)
                .and().in("payment_status", statusEnums);
        return xslOrderInfoExhibitionService.selectCount(numberWrap);
    }
}
