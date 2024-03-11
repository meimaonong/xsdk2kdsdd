package com.art.app.web.service.impl;

import com.art.app.common.Constants;
import com.art.app.common.enums.OrderPaymentStatusEnum;
import com.art.app.common.enums.OrderStatusEnum;
import com.art.app.common.enums.OrderTypeEnum;
import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.common.util.OrderUtils;
import com.art.app.orm.entity.Exhibition;
import com.art.app.orm.entity.TrainingClass;
import com.art.app.orm.entity.XslOrderInfo;
import com.art.app.orm.entity.XslOrderInfoExhibition;
import com.art.app.orm.service.ExhibitionService;
import com.art.app.orm.service.XslOrderInfoExhibitionService;
import com.art.app.web.bean.OrderException;
import com.art.app.web.bean.request.activity.ActivitySubListBizParam;
import com.art.app.web.bean.request.order.ClassOrderBizParam;
import com.art.app.web.bean.request.order.ExhibitionOrderBizParam;
import com.art.app.web.bean.request.order.OrderRequestVo;
import com.art.app.web.bean.response.activity.ActivitySubDetail;
import com.art.app.web.bean.response.activity.ActivitySubList;
import com.art.app.web.bean.response.activity.CommonActivityInfo;
import com.art.app.web.bean.OrderBasicInfo;
import com.art.app.web.service.AbstractOrderInfoService;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExhibitionOrderInfoServiceImpl extends AbstractOrderInfoService {

    @Resource
    private XslOrderInfoExhibitionService xslOrderInfoExhibitionService;
    @Resource
    private ExhibitionService exhibitionService;

    @Override
    public boolean matching(Integer orderType) {
        return OrderTypeEnum.EXHIBITION.getType() == orderType;
    }

    @Override
    public CommonActivityInfo queryCommonActivityInfo(int userId) {
        Wrapper wrapper = Condition.create()
                .eq("user_id", userId)
                .and().eq("del_flag", 0);
        int count = xslOrderInfoExhibitionService.selectCount(wrapper);
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
        XslOrderInfoExhibition orderInfo = xslOrderInfoExhibitionService.selectOne(wrapper);
        if (fromStatus.getStatus().equals(orderInfo.getOrderStatus())) {
            XslOrderInfoExhibition updateInfo = new XslOrderInfoExhibition();
            updateInfo.setId(orderInfo.getId());
            updateInfo.setPaymentStatus(toStatus.getStatus());
            updateInfo.setUpdatedAt(DatetimeUtils.now());
            xslOrderInfoExhibitionService.updateById(updateInfo);
        }
    }

    @Override
    public Integer countActivitySubList(ActivitySubListBizParam bizParams, int userId) {
        Wrapper wrapper = convertCountWrapper(bizParams, userId);
        return xslOrderInfoExhibitionService.selectCount(wrapper);
    }

    @Override
    public List<ActivitySubList> queryActivitySubList(ActivitySubListBizParam bizParams, int start, int userId) {
        Wrapper wrapper = convertListWrapper(bizParams, start, userId);
        List<XslOrderInfoExhibition> orderInfos = xslOrderInfoExhibitionService.selectList(wrapper);
        return orderInfos.stream().map(orderInfo -> {
            ActivitySubList activitySubList = new ActivitySubList();
            activitySubList.setOrderId(orderInfo.getOrderId());
            activitySubList.setResourceId(orderInfo.getExhibitionId());
            activitySubList.setTitle(orderInfo.getExhibitionName());
            activitySubList.setImgUrl(orderInfo.getThumbUrl());
            activitySubList.setCreatedAt(orderInfo.getCreatedAt());
            Exhibition exhibitionInfo = queryExhibitionInfo(orderInfo.getExhibitionId());
            activitySubList.setStatus(convertActivityStatus(orderInfo.getOrderStatus(), orderInfo.getPaymentStatus(),
                    exhibitionInfo.getApplyStartTime(), exhibitionInfo.getApplyStopTime()));
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
        XslOrderInfoExhibition orderInfo = xslOrderInfoExhibitionService.selectOne(wrapper);
        ActivitySubDetail result = new ActivitySubDetail();
        result.setOrderId(orderId);
        result.setResourceId(orderInfo.getExhibitionId());
        result.setPayType(orderInfo.getPaymentType());
        result.setTitle(orderInfo.getExhibitionName());
        result.setImgUrl(orderInfo.getThumbUrl());
        result.setPrice(orderInfo.getAmount());
        result.setPayment(orderInfo.getPayAmount());
        result.setCreatedAt(orderInfo.getCreatedAt());
        Exhibition exhibitionInfo = queryExhibitionInfo(orderInfo.getExhibitionId());
        result.setStatus(convertActivityStatus(orderInfo.getOrderStatus(), orderInfo.getPaymentStatus(),
                exhibitionInfo.getApplyStartTime(), exhibitionInfo.getApplyStopTime()));
        return result;
    }

    @Override
    public OrderBasicInfo queryOrderBasicInfo(String orderId) {
        Wrapper wrapper = Condition.create()
                .eq("order_id", orderId)
                .and().eq("del_flag", 0)
                .orderDesc(Collections.singleton("id"))
                .last("limit 1");
        XslOrderInfoExhibition orderInfo = xslOrderInfoExhibitionService.selectOne(wrapper);
        OrderBasicInfo result = new OrderBasicInfo();
        result.setOrderId(orderId);
        result.setOrderType(OrderTypeEnum.SKETCHING.getType());
        result.setOrderStatus(orderInfo.getOrderStatus());
        result.setPaymentStatus(orderInfo.getPaymentStatus());
        result.setSubject(orderInfo.getExhibitionName());
        result.setTimeExpire(DatetimeUtils.addMinute(DatetimeUtils.now(), Constants.MAX_PAYMENT_TIME));
        result.setCreatedAt(orderInfo.getCreatedAt());
        return result;
    }

    @Override
    protected XslOrderInfo buildXslOrderInfo(OrderRequestVo requestVo, int userId) {
        ClassOrderBizParam orderBizParams = (ClassOrderBizParam) requestVo.getOrderBizParams(ClassOrderBizParam.class);
        String resourceId = orderBizParams.getResourceId();
        Exhibition exhibitionInfo = queryExhibitionInfo(resourceId);
//        Date nowDate = DatetimeUtils.now();
        if (Objects.nonNull(exhibitionInfo)) {
//            if (null != exhibitionInfo.getApplyStartTime()
//                    && nowDate.after(exhibitionInfo.getApplyStartTime())
//                    && null != exhibitionInfo.getApplyStopTime()
//                    && nowDate.before(exhibitionInfo.getApplyStopTime())) {
            // 必须在报名时间之间报名 --- 1期不需要这个逻辑
            if (null != exhibitionInfo.getPrice() && BigDecimal.ZERO.compareTo(exhibitionInfo.getPrice()) <= 0) {
                // 价格必须大于等于0
                XslOrderInfo xslOrderInfo = new XslOrderInfo();
                xslOrderInfo.setUserId(userId);
                xslOrderInfo.setOrderId(OrderUtils.createOrderId("EXB"));
                xslOrderInfo.setOrderType(OrderTypeEnum.EXHIBITION.getType());
                xslOrderInfo.setRemark("");
                xslOrderInfo.setAmount(exhibitionInfo.getPrice());
                xslOrderInfo.setCreatedAt(DatetimeUtils.now());
                xslOrderInfo.setUpdatedAt(xslOrderInfo.getCreatedAt());
                xslOrderInfo.setDelFlag(0);
                return xslOrderInfo;
            } else {
                throw new OrderException(ResponseCodeEnum.EXHIBITION_APPLY_PRICE_ERROR);
            }
//            } else {
//                throw new OrderException(ResponseCodeEnum.EXHIBITION_APPLY_TIME_ERROR);
//            }
        } else {
            throw new OrderException(ResponseCodeEnum.EXHIBITION_INVALID_ERROR);
        }
    }

    @Override
    protected void saveOrderDetailInfo(OrderRequestVo requestVo, int userId, String orderId) {
        ExhibitionOrderBizParam orderBizParams = (ExhibitionOrderBizParam) requestVo.getOrderBizParams(ExhibitionOrderBizParam.class);
        String resourceId = orderBizParams.getResourceId();
        Exhibition exhibitionInfo = queryExhibitionInfo(resourceId);
        XslOrderInfoExhibition orderInfo = new XslOrderInfoExhibition();
        orderInfo.setUserId(userId);
        orderInfo.setOrderId(orderId);
        orderInfo.setOrderStatus(OrderStatusEnum.APPLYING.getType());
        orderInfo.setPaymentStatus(OrderPaymentStatusEnum.NOT_PAID.getStatus());
        orderInfo.setAmount(exhibitionInfo.getPrice());
        orderInfo.setPayAmount(exhibitionInfo.getPrice());
        orderInfo.setExhibitionId(exhibitionInfo.getSnId());
        orderInfo.setExhibitionName(exhibitionInfo.getTitle());
        orderInfo.setThumbUrl(exhibitionInfo.getThumbUrl());
        orderInfo.setExhibitionStartTime(exhibitionInfo.getClassStartTime());
        orderInfo.setExhibitionEndTime(exhibitionInfo.getClassStopTime());
        orderInfo.setName(orderBizParams.getName());
        orderInfo.setPhone(orderBizParams.getPhone());
        orderInfo.setRemark(orderBizParams.getRemark());
        if (!CollectionUtils.isEmpty(orderBizParams.getWorks())) {
            orderInfo.setWorks(String.join(",", orderBizParams.getWorks()));
        }
        orderInfo.setCreatedAt(DatetimeUtils.now());
        orderInfo.setUpdatedAt(orderInfo.getCreatedAt());
        orderInfo.setDelFlag(0);
        xslOrderInfoExhibitionService.insert(orderInfo);
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
