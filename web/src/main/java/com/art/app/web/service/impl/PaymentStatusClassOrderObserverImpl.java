package com.art.app.web.service.impl;

import com.art.app.common.enums.OrderPaymentStatusEnum;
import com.art.app.common.enums.OrderStatusEnum;
import com.art.app.common.enums.OrderTypeEnum;
import com.art.app.common.pattern.entity.PaymentStatusParams;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.orm.entity.TrainingClass;
import com.art.app.orm.entity.XslOrderInfoClass;
import com.art.app.orm.service.TrainingClassService;
import com.art.app.orm.service.XslOrderInfoClassService;
import com.art.app.web.service.AbstractPaymentStatusObserver;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;

@Service
@Slf4j
public class PaymentStatusClassOrderObserverImpl extends AbstractPaymentStatusObserver {

    @Resource
    private XslOrderInfoClassService xslOrderInfoClassService;
    @Resource
    private TrainingClassService trainingClassService;

    @Override
    public boolean support(PaymentStatusParams params) {
        return OrderTypeEnum.CLASS.getType() == params.getArtOrderType();
    }

    @Override
    protected void updateOrderStatus(PaymentStatusParams params, OrderStatusEnum orderStatusEnum, OrderPaymentStatusEnum orderPaymentStatusEnum) {
        Wrapper wrapper = Condition.create()
                .eq("order_id", params.getArtOrderId())
                .and().eq("del_flag", 0);
        XslOrderInfoClass orderInfo = xslOrderInfoClassService.selectOne(wrapper);
        if (null != orderInfo) {
            XslOrderInfoClass updateInfo = new XslOrderInfoClass();
            updateInfo.setId(orderInfo.getId());
            updateInfo.setOrderStatus(orderStatusEnum.getType());
            updateInfo.setPaymentStatus(orderPaymentStatusEnum.getStatus());
            updateInfo.setPaymentType(params.getPaymentType());
            updateInfo.setUpdatedAt(DatetimeUtils.now());
            xslOrderInfoClassService.updateById(updateInfo);
            TrainingClass trainingClass = queryTrainingClassInfo(orderInfo.getClassId());
            if (1 == trainingClass.getIsGroup()) {
                // 拼团订单
                int assembleId = saveAssembleInfo(orderInfo.getAssembleId(), trainingClass.getSnId(),
                        trainingClass.getTitle(), trainingClass.getApplyStopTime(), orderInfo.getUserId());
                if (0 == orderInfo.getAssembleId()) {
                    XslOrderInfoClass updateAssembleInfo = new XslOrderInfoClass();
                    updateAssembleInfo.setId(orderInfo.getId());
                    updateAssembleInfo.setAssembleId(assembleId);
                    updateAssembleInfo.setUpdatedAt(DatetimeUtils.now());
                    xslOrderInfoClassService.updateById(updateAssembleInfo);
                }
            }
        }
    }

    private TrainingClass queryTrainingClassInfo(String resourceId) {
        Wrapper wrapper = Condition.create()
                .eq("del_flag", 0)
                .and().eq("sn_id", resourceId)
                .orderDesc(Collections.singleton("id"))
                .last("limit 1");
        TrainingClass trainingClass = trainingClassService.selectOne(wrapper);
        return trainingClass;
    }

}
