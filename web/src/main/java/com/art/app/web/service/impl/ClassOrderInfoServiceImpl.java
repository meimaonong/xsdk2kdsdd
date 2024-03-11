package com.art.app.web.service.impl;

import com.art.app.common.Constants;
import com.art.app.common.enums.OrderPaymentStatusEnum;
import com.art.app.common.enums.OrderStatusEnum;
import com.art.app.common.enums.OrderTypeEnum;
import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.common.util.OrderUtils;
import com.art.app.orm.entity.TrainingClass;
import com.art.app.orm.entity.XslOrderInfo;
import com.art.app.orm.entity.XslOrderInfoClass;
import com.art.app.orm.service.TrainingClassService;
import com.art.app.orm.service.XslOrderInfoClassService;
import com.art.app.web.bean.OrderBasicInfo;
import com.art.app.web.bean.OrderException;
import com.art.app.web.bean.request.activity.ActivitySubListBizParam;
import com.art.app.web.bean.request.order.ClassOrderBizParam;
import com.art.app.web.bean.request.order.OrderRequestVo;
import com.art.app.web.bean.response.activity.ActivitySubDetail;
import com.art.app.web.bean.response.activity.ActivitySubList;
import com.art.app.web.bean.response.activity.CommonActivityInfo;
import com.art.app.web.service.AbstractOrderInfoService;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClassOrderInfoServiceImpl extends AbstractOrderInfoService {

    @Resource
    private XslOrderInfoClassService xslOrderInfoClassService;
    @Resource
    private TrainingClassService trainingClassService;

    @Override
    public boolean matching(Integer orderType) {
        return OrderTypeEnum.CLASS.getType() == orderType;
    }

    @Override
    public CommonActivityInfo queryCommonActivityInfo(int userId) {
        Wrapper wrapper = Condition.create()
                .eq("user_id", userId)
                .and().eq("del_flag", 0);
        int count = xslOrderInfoClassService.selectCount(wrapper);
        CommonActivityInfo info = new CommonActivityInfo();
        info.setOrderNumbers(count);
        return info;
    }

    @Override
    public void updateOrderPaymentStatus(XslOrderInfo xslOrderInfo, OrderPaymentStatusEnum fromStatus, OrderPaymentStatusEnum toStatus) {
        Wrapper wrapper = Condition.create()
                .eq("order_id", xslOrderInfo.getOrderId())
                .and().eq("del_flag", 0)
                .orderDesc(Collections.singleton("id"))
                .last("limit 1");
        XslOrderInfoClass orderInfoClass = xslOrderInfoClassService.selectOne(wrapper);
        if (fromStatus.getStatus().equals(orderInfoClass.getOrderStatus())) {
            XslOrderInfoClass updateInfo = new XslOrderInfoClass();
            updateInfo.setId(orderInfoClass.getId());
            updateInfo.setPaymentStatus(toStatus.getStatus());
            updateInfo.setUpdatedAt(DatetimeUtils.now());
            xslOrderInfoClassService.updateById(updateInfo);
        }
    }

    @Override
    public Integer countActivitySubList(ActivitySubListBizParam bizParams, int userId) {
        Wrapper wrapper = convertCountWrapper(bizParams, userId);
        return xslOrderInfoClassService.selectCount(wrapper);
    }

    @Override
    public List<ActivitySubList> queryActivitySubList(ActivitySubListBizParam bizParams, int start, int userId) {
        Wrapper wrapper = convertListWrapper(bizParams, start, userId);
        List<XslOrderInfoClass> orderInfos = xslOrderInfoClassService.selectList(wrapper);
        return orderInfos.stream().map(orderInfo -> {
            ActivitySubList activitySubList = new ActivitySubList();
            activitySubList.setOrderId(orderInfo.getOrderId());
            activitySubList.setResourceId(orderInfo.getClassId());
            activitySubList.setTitle(orderInfo.getClassName());
            activitySubList.setImgUrl(orderInfo.getThumbUrl());
            activitySubList.setCreatedAt(orderInfo.getCreatedAt());
            TrainingClass trainingClass = queryTrainingClassInfo(orderInfo.getClassId());
            activitySubList.setStatus(convertActivityStatus(orderInfo.getOrderStatus(), orderInfo.getPaymentStatus(),
                    trainingClass.getApplyStartTime(), trainingClass.getApplyStopTime()));
            return activitySubList;
        }).collect(Collectors.toList());
    }

    @Override
    public ActivitySubDetail queryActivitySubDetail(String orderId) {
        Wrapper wrapper = Condition.create()
                .eq("order_id", orderId)
                .and().eq("del_flag", 0)
                .orderDesc(Collections.singleton("id"))
                .last("limit 1");
        XslOrderInfoClass orderInfo = xslOrderInfoClassService.selectOne(wrapper);
        ActivitySubDetail result = new ActivitySubDetail();
        result.setOrderId(orderId);
        result.setResourceId(orderInfo.getClassId());
        result.setPayType(orderInfo.getPaymentType());
        result.setTitle(orderInfo.getClassName());
        result.setImgUrl(orderInfo.getThumbUrl());
        result.setPrice(orderInfo.getAmount());
        result.setPayment(orderInfo.getPayAmount());
        result.setCreatedAt(orderInfo.getCreatedAt());
        TrainingClass trainingClass = queryTrainingClassInfo(orderInfo.getClassId());
        result.setStatus(convertActivityStatus(orderInfo.getOrderStatus(), orderInfo.getPaymentStatus(),
                trainingClass.getApplyStartTime(), trainingClass.getApplyStopTime()));
        return result;
    }

    @Override
    public OrderBasicInfo queryOrderBasicInfo(String orderId) {
        Wrapper wrapper = Condition.create()
                .eq("order_id", orderId)
                .and().eq("del_flag", 0)
                .orderDesc(Collections.singleton("id"))
                .last("limit 1");
        XslOrderInfoClass orderInfo = xslOrderInfoClassService.selectOne(wrapper);
        OrderBasicInfo result = new OrderBasicInfo();
        result.setOrderId(orderId);
        result.setOrderType(OrderTypeEnum.CLASS.getType());
        result.setOrderStatus(orderInfo.getOrderStatus());
        result.setPaymentStatus(orderInfo.getPaymentStatus());
        result.setSubject(orderInfo.getClassName());
        result.setTimeExpire(DatetimeUtils.addMinute(DatetimeUtils.now(), Constants.MAX_PAYMENT_TIME));
        result.setCreatedAt(orderInfo.getCreatedAt());
        return result;
    }

    @Override
    protected XslOrderInfo buildXslOrderInfo(OrderRequestVo requestVo, int userId) {
        ClassOrderBizParam orderBizParams = (ClassOrderBizParam) requestVo.getOrderBizParams(ClassOrderBizParam.class);
        String resourceId = orderBizParams.getResourceId();
        TrainingClass trainingClass = queryTrainingClassInfo(resourceId);
//        Date nowDate = DatetimeUtils.now();
        if (Objects.nonNull(trainingClass)) {
//            if (null != trainingClass.getApplyStartTime()
//                    && nowDate.after(trainingClass.getApplyStartTime())
//                    && null != trainingClass.getApplyStopTime()
//                    && nowDate.before(trainingClass.getApplyStopTime())) {
            // 必须在报名时间之间报名 --- 1期不需要这个逻辑
            if (null != trainingClass.getPrice() && BigDecimal.ZERO.compareTo(trainingClass.getPrice()) <= 0) {
                // 价格必须大于等于0
                XslOrderInfo xslOrderInfo = new XslOrderInfo();
                xslOrderInfo.setUserId(userId);
                xslOrderInfo.setOrderId(OrderUtils.createOrderId("CLS"));
                xslOrderInfo.setOrderType(OrderTypeEnum.CLASS.getType());
                xslOrderInfo.setRemark("");
                xslOrderInfo.setAmount(trainingClass.getPrice());
                xslOrderInfo.setCreatedAt(DatetimeUtils.now());
                xslOrderInfo.setUpdatedAt(xslOrderInfo.getCreatedAt());
                xslOrderInfo.setDelFlag(0);
                return xslOrderInfo;
            } else {
                throw new OrderException(ResponseCodeEnum.TRAINING_CLASS_APPLY_PRICE_ERROR);
            }
//            } else {
//                throw new OrderException(ResponseCodeEnum.TRAINING_CLASS_APPLY_TIME_ERROR);
//            }
        } else {
            throw new OrderException(ResponseCodeEnum.TRAINING_CLASS_INVALID_ERROR);
        }
    }

    @Override
    protected void saveOrderDetailInfo(OrderRequestVo requestVo, int userId, String orderId) {
        ClassOrderBizParam orderBizParams = (ClassOrderBizParam) requestVo.getOrderBizParams(ClassOrderBizParam.class);
        TrainingClass trainingClass = queryTrainingClassInfo(orderBizParams.getResourceId());
        XslOrderInfoClass classOrderInfo = new XslOrderInfoClass();
        classOrderInfo.setUserId(userId);
        classOrderInfo.setOrderId(orderId);
        classOrderInfo.setOrderStatus(OrderStatusEnum.APPLYING.getType());
        classOrderInfo.setPaymentStatus(OrderPaymentStatusEnum.NOT_PAID.getStatus());
        classOrderInfo.setAmount(trainingClass.getPrice());
        classOrderInfo.setPayAmount(trainingClass.getPrice());
        classOrderInfo.setClassId(trainingClass.getSnId());
        classOrderInfo.setClassName(trainingClass.getTitle());
        classOrderInfo.setThumbUrl(trainingClass.getThumbUrl());
        classOrderInfo.setClassStartTime(trainingClass.getClassStartTime());
        classOrderInfo.setClassEndTime(trainingClass.getClassStopTime());
        classOrderInfo.setAssembleId(orderBizParams.getAssembleId());
        classOrderInfo.setName(orderBizParams.getName());
        classOrderInfo.setPhone(orderBizParams.getPhone());
        classOrderInfo.setRemark(orderBizParams.getRemark());
        if (!CollectionUtils.isEmpty(orderBizParams.getWorks())) {
            classOrderInfo.setWorks(String.join(",", orderBizParams.getWorks()));
        }
        classOrderInfo.setCreatedAt(DatetimeUtils.now());
        classOrderInfo.setUpdatedAt(classOrderInfo.getCreatedAt());
        classOrderInfo.setDelFlag(0);
        xslOrderInfoClassService.insert(classOrderInfo);

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
