package com.art.app.web.service;

import com.art.app.common.enums.OrderPaymentStatusEnum;
import com.art.app.common.enums.OrderStatusEnum;
import com.art.app.common.enums.PaymentStatusEnum;
import com.art.app.common.enums.ResourceTypeEnum;
import com.art.app.common.pattern.IPaymentStatusObserver;
import com.art.app.common.pattern.entity.PaymentStatusParams;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.orm.entity.Assemble;
import com.art.app.orm.entity.AssembleInfo;
import com.art.app.orm.service.AssembleInfoService;
import com.art.app.orm.service.AssembleService;
import com.art.app.payment.utils.JackJsonUtil;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
public abstract class AbstractPaymentStatusObserver implements IPaymentStatusObserver {

    @Resource
    private AssembleInfoService assembleInfoService;
    @Resource
    private AssembleService assembleService;

    @Override
    public void deal(PaymentStatusParams params) {
        OrderStatusEnum orderStatusEnum = convertOrderStatusEnum(params.getPaymentStatus());
        if (OrderStatusEnum.APPLY_SUCCESS.getType() == orderStatusEnum.getType()) {
            updateOrderStatus(params, orderStatusEnum, OrderPaymentStatusEnum.SUCCESS);
        } else {
            log.info("支付状态不是成功状态 -> {}", JackJsonUtil.toJson(params));
            // TODO 提醒支付失败

        }
    }

    protected int saveAssembleInfo(Integer assembleId, String snId, String title, Date applyStopTime, int userId) {
        Boolean isSponsor = false;
        if (0 == assembleId) {
            // 新建拼团
            isSponsor = true;
            AssembleInfo assembleInsertInfo = new AssembleInfo();
            assembleInsertInfo.setType(ResourceTypeEnum.TRAINING.getType());
            assembleInsertInfo.setSnId(snId);
            assembleInsertInfo.setTitle(title);
            assembleInsertInfo.setApplyStopTime(applyStopTime);
            assembleInsertInfo.setCount(1);
            assembleInsertInfo.setUserId(userId);
            assembleInsertInfo.setCreatedAt(DatetimeUtils.now());
            assembleInsertInfo.setUpdatedAt(assembleInsertInfo.getCreatedAt());
            assembleInsertInfo.setDelFlag(0);
            assembleInfoService.insert(assembleInsertInfo);
            assembleId = assembleInsertInfo.getId();
        } else {
            // 更新拼团信息
            Wrapper assembleInfoWrapper = Condition.create()
                    .eq("del_flag", 0)
                    .and().eq("id", assembleId);
            AssembleInfo assembleInfo = assembleInfoService.selectOne(assembleInfoWrapper);
            assembleInfoService.updateCountById(assembleInfo.getId(), 1);
        }
        Assemble assemble = new Assemble();
        assemble.setUserId(userId);
        assemble.setAssembleId(assembleId);
        assemble.setSponsor(isSponsor ? 1 : 0);
        assemble.setCreatedAt(DatetimeUtils.now());
        assemble.setUpdatedAt(assemble.getCreatedAt());
        assemble.setDelFlag(0);
        assembleService.insert(assemble);
        return assembleId;
    }

    protected abstract void updateOrderStatus(PaymentStatusParams params, OrderStatusEnum orderStatusEnum, OrderPaymentStatusEnum orderPaymentStatusEnum);

    private OrderStatusEnum convertOrderStatusEnum(Integer paymentStatus) {
        OrderStatusEnum result = null;
        if (PaymentStatusEnum.PAY_SUCCESS.getStatus().equals(paymentStatus)) {
            result = OrderStatusEnum.APPLY_SUCCESS;
        } else if (PaymentStatusEnum.PAY_FAILED.getStatus().equals(paymentStatus)) {
            result = OrderStatusEnum.APPLY_FAIL;
        } else if (PaymentStatusEnum.CANCELED.getStatus().equals(paymentStatus)) {
            result = OrderStatusEnum.APPLY_FAIL;
        } else {
            result = OrderStatusEnum.UNKNOWN;
        }
        return result;
    }

    private OrderPaymentStatusEnum convertOrderPaymentStatusEnum(Integer paymentStatus) {
        OrderPaymentStatusEnum result = null;
        if (PaymentStatusEnum.PAY_SUCCESS.getStatus().equals(paymentStatus)) {
            result = OrderPaymentStatusEnum.SUCCESS;
        } else if (PaymentStatusEnum.PAY_FAILED.getStatus().equals(paymentStatus)) {
            result = OrderPaymentStatusEnum.FAILED;
        } else if (PaymentStatusEnum.CANCELED.getStatus().equals(paymentStatus)) {
            result = OrderPaymentStatusEnum.CANCEL;
        } else {
            result = OrderPaymentStatusEnum.UNKNOWN;
        }
        return result;
    }

}
