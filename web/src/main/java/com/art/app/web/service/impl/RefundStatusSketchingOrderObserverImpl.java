package com.art.app.web.service.impl;

import com.art.app.common.enums.OrderPaymentStatusEnum;
import com.art.app.common.enums.OrderTypeEnum;
import com.art.app.common.pattern.entity.RefundStatusParams;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.orm.entity.AssembleInfo;
import com.art.app.orm.entity.XslOrderInfoSketching;
import com.art.app.orm.service.AssembleInfoService;
import com.art.app.orm.service.XslOrderInfoSketchingService;
import com.art.app.web.service.AbstractRefundStatusObserver;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class RefundStatusSketchingOrderObserverImpl extends AbstractRefundStatusObserver {

    @Resource
    private XslOrderInfoSketchingService xslOrderInfoSketchingService;
    @Resource
    private AssembleInfoService assembleInfoService;

    @Override
    public boolean support(RefundStatusParams params) {
        return OrderTypeEnum.SKETCHING.getType() == params.getArtOrderType();
    }

    @Override
    protected void updateOrderPaymentStatus(RefundStatusParams params, OrderPaymentStatusEnum orderPaymentStatusEnum) {
        Wrapper wrapper = Condition.create()
                .eq("order_id", params.getArtOrderId())
                .and().eq("del_flag", 0);
        XslOrderInfoSketching orderInfo = xslOrderInfoSketchingService.selectOne(wrapper);
        if (null != orderInfo) {
            XslOrderInfoSketching updateInfo = new XslOrderInfoSketching();
            updateInfo.setId(orderInfo.getId());
            updateInfo.setPaymentStatus(orderPaymentStatusEnum.getStatus());
            updateInfo.setUpdatedAt(DatetimeUtils.now());
            xslOrderInfoSketchingService.updateById(updateInfo);
            if (0 < orderInfo.getAssembleId()) {
                // 拼团订单退款，更新拼团信息
                Wrapper assembleInfoWrapper = Condition.create()
                        .eq("del_flag", 0)
                        .and().eq("id", orderInfo.getAssembleId());
                AssembleInfo assembleInfo = assembleInfoService.selectOne(assembleInfoWrapper);
                assembleInfoService.updateCountById(assembleInfo.getId(), -1);
            }
        }
    }

}
