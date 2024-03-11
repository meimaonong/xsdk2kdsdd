package com.art.app.web.service.impl;

import com.art.app.common.enums.OrderPaymentStatusEnum;
import com.art.app.common.enums.OrderStatusEnum;
import com.art.app.common.enums.OrderTypeEnum;
import com.art.app.common.pattern.entity.PaymentStatusParams;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.orm.entity.SketchingActivity;
import com.art.app.orm.entity.XslOrderInfoSketching;
import com.art.app.orm.service.SketchingActivityService;
import com.art.app.orm.service.XslOrderInfoSketchingService;
import com.art.app.web.service.AbstractPaymentStatusObserver;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;

@Service
@Slf4j
public class PaymentStatusSketchingOrderObserverImpl extends AbstractPaymentStatusObserver {

    @Resource
    private XslOrderInfoSketchingService xslOrderInfoSketchingService;
    @Resource
    private SketchingActivityService sketchingActivityService;

    @Override
    public boolean support(PaymentStatusParams params) {
        return OrderTypeEnum.SKETCHING.getType() == params.getArtOrderType();
    }

    @Override
    protected void updateOrderStatus(PaymentStatusParams params, OrderStatusEnum orderStatusEnum, OrderPaymentStatusEnum orderPaymentStatusEnum) {
        Wrapper wrapper = Condition.create()
                .eq("order_id", params.getArtOrderId())
                .and().eq("del_flag", 0);
        XslOrderInfoSketching orderInfo = xslOrderInfoSketchingService.selectOne(wrapper);
        if (null != orderInfo) {
            XslOrderInfoSketching updateInfo = new XslOrderInfoSketching();
            updateInfo.setId(orderInfo.getId());
            updateInfo.setOrderStatus(orderStatusEnum.getType());
            updateInfo.setPaymentStatus(orderPaymentStatusEnum.getStatus());
            updateInfo.setPaymentType(params.getPaymentType());
            updateInfo.setUpdatedAt(DatetimeUtils.now());
            xslOrderInfoSketchingService.updateById(updateInfo);
            SketchingActivity sketchingActivityInfo = querySketchingActivityInfo(orderInfo.getSketchingId());
            if (1 == sketchingActivityInfo.getIsGroup()) {
                int assembleId = saveAssembleInfo(orderInfo.getAssembleId(), sketchingActivityInfo.getSnId(),
                        sketchingActivityInfo.getTitle(), sketchingActivityInfo.getApplyStopTime(), orderInfo.getUserId());
                if (0 == orderInfo.getAssembleId()) {
                    XslOrderInfoSketching updateAssembleInfo = new XslOrderInfoSketching();
                    updateAssembleInfo.setId(orderInfo.getId());
                    updateAssembleInfo.setAssembleId(assembleId);
                    updateAssembleInfo.setUpdatedAt(DatetimeUtils.now());
                    xslOrderInfoSketchingService.updateById(updateAssembleInfo);
                }
            }
        }
    }

    private SketchingActivity querySketchingActivityInfo(String resourceId) {
        Wrapper wrapper = Condition.create()
                .eq("del_flag", 0)
                .and().eq("sn_id", resourceId)
                .orderDesc(Collections.singleton("id"))
                .last("limit 1");
        SketchingActivity sketchingActivityInfo = sketchingActivityService.selectOne(wrapper);
        return sketchingActivityInfo;
    }

}
