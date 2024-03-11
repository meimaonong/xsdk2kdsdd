package com.art.app.web.service.impl;

import com.art.app.common.enums.OrderPaymentStatusEnum;
import com.art.app.common.enums.OrderTypeEnum;
import com.art.app.common.pattern.entity.RefundStatusParams;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.orm.entity.XslOrderInfoExhibition;
import com.art.app.orm.service.XslOrderInfoExhibitionService;
import com.art.app.web.service.AbstractRefundStatusObserver;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class RefundStatusExhibitionOrderObserverImpl extends AbstractRefundStatusObserver {

    @Resource
    private XslOrderInfoExhibitionService xslOrderInfoExhibitionService;


    @Override
    public boolean support(RefundStatusParams params) {
        return OrderTypeEnum.EXHIBITION.getType() == params.getArtOrderType();
    }

    @Override
    protected void updateOrderPaymentStatus(RefundStatusParams params, OrderPaymentStatusEnum orderPaymentStatusEnum) {
        Wrapper wrapper = Condition.create()
                .eq("order_id", params.getArtOrderId())
                .and().eq("del_flag", 0);
        XslOrderInfoExhibition orderInfo = xslOrderInfoExhibitionService.selectOne(wrapper);
        if (null != orderInfo) {
            XslOrderInfoExhibition updateInfo = new XslOrderInfoExhibition();
            updateInfo.setId(orderInfo.getId());
            updateInfo.setPaymentStatus(orderPaymentStatusEnum.getStatus());
            updateInfo.setUpdatedAt(DatetimeUtils.now());
            xslOrderInfoExhibitionService.updateById(updateInfo);
        }
    }

}
