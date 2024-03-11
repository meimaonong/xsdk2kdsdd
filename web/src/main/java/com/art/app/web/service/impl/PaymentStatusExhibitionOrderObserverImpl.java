package com.art.app.web.service.impl;

import com.art.app.common.enums.OrderPaymentStatusEnum;
import com.art.app.common.enums.OrderStatusEnum;
import com.art.app.common.enums.OrderTypeEnum;
import com.art.app.common.pattern.entity.PaymentStatusParams;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.orm.entity.Exhibition;
import com.art.app.orm.entity.XslOrderInfoExhibition;
import com.art.app.orm.service.ExhibitionService;
import com.art.app.orm.service.XslOrderInfoExhibitionService;
import com.art.app.web.service.AbstractPaymentStatusObserver;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;

@Service
@Slf4j
public class PaymentStatusExhibitionOrderObserverImpl extends AbstractPaymentStatusObserver {

    @Resource
    private XslOrderInfoExhibitionService xslOrderInfoExhibitionService;
    @Resource
    private ExhibitionService exhibitionService;

    @Override
    public boolean support(PaymentStatusParams params) {
        return OrderTypeEnum.EXHIBITION.getType() == params.getArtOrderType();
    }

    @Override
    protected void updateOrderStatus(PaymentStatusParams params, OrderStatusEnum orderStatusEnum, OrderPaymentStatusEnum orderPaymentStatusEnum) {
        Wrapper wrapper = Condition.create()
                .eq("order_id", params.getArtOrderId())
                .and().eq("del_flag", 0);
        XslOrderInfoExhibition orderInfo = xslOrderInfoExhibitionService.selectOne(wrapper);
        if (null != orderInfo) {
            XslOrderInfoExhibition updateInfo = new XslOrderInfoExhibition();
            updateInfo.setId(orderInfo.getId());
            updateInfo.setOrderStatus(orderStatusEnum.getType());
            updateInfo.setPaymentStatus(orderPaymentStatusEnum.getStatus());
            updateInfo.setPaymentType(params.getPaymentType());
            updateInfo.setUpdatedAt(DatetimeUtils.now());
            xslOrderInfoExhibitionService.updateById(updateInfo);
            Exhibition exhibitionInfo = queryExhibitionInfo(orderInfo.getExhibitionId());
            if (1 == exhibitionInfo.getIsGroup()) {
                // 拼团订单
                int assembleId = saveAssembleInfo(orderInfo.getAssembleId(), exhibitionInfo.getSnId(),
                        exhibitionInfo.getTitle(), exhibitionInfo.getApplyStopTime(), orderInfo.getUserId());
                if (0 == orderInfo.getAssembleId()) {
                    XslOrderInfoExhibition updateAssembleInfo = new XslOrderInfoExhibition();
                    updateAssembleInfo.setId(orderInfo.getId());
                    updateAssembleInfo.setAssembleId(assembleId);
                    updateAssembleInfo.setUpdatedAt(DatetimeUtils.now());
                    xslOrderInfoExhibitionService.updateById(updateAssembleInfo);
                }
            }
        }
    }

    private Exhibition queryExhibitionInfo(String resourceId) {
        Wrapper wrapper = Condition.create()
                .eq("del_flag", 0)
                .and().eq("sn_id", resourceId)
                .orderDesc(Collections.singleton("id"))
                .last("limit 1");
        Exhibition exhibitionInfo = exhibitionService.selectOne(wrapper);
        return exhibitionInfo;
    }

}
