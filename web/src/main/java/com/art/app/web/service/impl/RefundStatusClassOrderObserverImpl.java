package com.art.app.web.service.impl;

import com.art.app.common.enums.OrderPaymentStatusEnum;
import com.art.app.common.enums.OrderTypeEnum;
import com.art.app.common.pattern.entity.RefundStatusParams;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.orm.entity.AssembleInfo;
import com.art.app.orm.entity.XslOrderInfoClass;
import com.art.app.orm.service.AssembleInfoService;
import com.art.app.orm.service.XslOrderInfoClassService;
import com.art.app.web.service.AbstractRefundStatusObserver;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class RefundStatusClassOrderObserverImpl extends AbstractRefundStatusObserver {

    @Resource
    private XslOrderInfoClassService xslOrderInfoClassService;
    @Resource
    private AssembleInfoService assembleInfoService;

    @Override
    public boolean support(RefundStatusParams params) {
        return OrderTypeEnum.CLASS.getType() == params.getArtOrderType();
    }

    @Override
    protected void updateOrderPaymentStatus(RefundStatusParams params, OrderPaymentStatusEnum orderPaymentStatusEnum) {
        Wrapper wrapper = Condition.create()
                .eq("order_id", params.getArtOrderId())
                .and().eq("del_flag", 0);
        XslOrderInfoClass classOrderInfo = xslOrderInfoClassService.selectOne(wrapper);
        if (null != classOrderInfo) {
            XslOrderInfoClass updateInfo = new XslOrderInfoClass();
            updateInfo.setId(classOrderInfo.getId());
            updateInfo.setPaymentStatus(orderPaymentStatusEnum.getStatus());
            updateInfo.setUpdatedAt(DatetimeUtils.now());
            xslOrderInfoClassService.updateById(updateInfo);
            if (0 < classOrderInfo.getAssembleId()) {
                // 拼团订单退款，更新拼团信息
                Wrapper assembleInfoWrapper = Condition.create()
                        .eq("del_flag", 0)
                        .and().eq("id", classOrderInfo.getAssembleId());
                AssembleInfo assembleInfo = assembleInfoService.selectOne(assembleInfoWrapper);
                assembleInfoService.updateCountById(assembleInfo.getId(), -1);
            }
        }
    }

}
