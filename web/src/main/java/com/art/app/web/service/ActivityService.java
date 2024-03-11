package com.art.app.web.service;

import com.art.app.common.bean.FactoryList;
import com.art.app.common.enums.OrderTypeEnum;
import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.common.util.TokenUtils;
import com.art.app.orm.entity.XslOrderInfo;
import com.art.app.orm.service.XslOrderInfoService;
import com.art.app.web.bean.OrderException;
import com.art.app.web.bean.request.activity.ActivityListBizParam;
import com.art.app.web.bean.request.activity.ActivitySubDetailBizParam;
import com.art.app.web.bean.request.activity.ActivitySubListBizParam;
import com.art.app.web.bean.request.order.OrderRequestVo;
import com.art.app.web.bean.response.XslList;
import com.art.app.web.bean.response.activity.ActivityList;
import com.art.app.web.bean.response.activity.ActivitySubDetail;
import com.art.app.web.bean.response.activity.ActivitySubList;
import com.art.app.web.bean.response.activity.CommonActivityInfo;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    @Resource
    private XslOrderInfoService xslOrderInfoService;
    @Resource
    private FactoryList<Integer, IOrderInfoService> iOrderInfoServices;

    public XslList<ActivityList> queryActivityList(OrderRequestVo<ActivityListBizParam> requestVo) {
        int userId = TokenUtils.getUserIdByToken(requestVo.getBaseParams().getToken());
        XslList<ActivityList> result = new XslList<>();
        result.setHasMore(false);
        List<ActivityList> activityList = OrderTypeEnum.getActivityList().stream().map(orderTypeEnum -> {
            ActivityList activity = new ActivityList();
            activity.setType(orderTypeEnum.getType());
            CommonActivityInfo info = iOrderInfoServices.getBean(orderTypeEnum.getType()).queryCommonActivityInfo(userId);
            activity.setTitle(orderTypeEnum.getName());
            activity.setDetail("共参与" + info.getOrderNumbers() + "次");
            return activity;
        }).collect(Collectors.toList());
        result.setContents(activityList);
        return result;
    }

    public XslList<ActivitySubList> queryActivitySubList(OrderRequestVo<ActivitySubListBizParam> requestVo) {
        XslList<ActivitySubList> result = new XslList<>();
        int userId = TokenUtils.getUserIdByToken(requestVo.getBaseParams().getToken());
        ActivitySubListBizParam bizParams = requestVo.getOrderBizParams(ActivitySubListBizParam.class);
        Integer pageIndex = (null == bizParams.getPageIndex()) ? 0 : bizParams.getPageIndex();
        bizParams.setPageIndex(pageIndex);
        Integer totalNumbers = iOrderInfoServices.getBean(bizParams.getType()).countActivitySubList(bizParams, userId);
        Integer start = (bizParams.getPageIndex() - 1) * bizParams.getPageSize();
        List<ActivitySubList> contents = iOrderInfoServices.getBean(bizParams.getType()).queryActivitySubList(bizParams, start, userId);
        if (totalNumbers > (start + contents.size())) {
            result.setHasMore(true);
        } else {
            result.setHasMore(false);
        }
        result.setContents(contents);
        return result;
    }

    public ActivitySubDetail queryActivitySubDetail(OrderRequestVo<ActivitySubDetailBizParam> requestVo) {
        ActivitySubDetailBizParam bizParams = requestVo.getOrderBizParams(ActivitySubDetailBizParam.class);
        String orderId = bizParams.getOrderId();
        Wrapper wrapper = Condition.create()
                .eq("order_id", orderId)
                .and().eq("del_flag", 0)
                .orderDesc(Collections.singleton("id"))
                .last("limit 1");
        XslOrderInfo xslOrderInfo = xslOrderInfoService.selectOne(wrapper);
        if (null == xslOrderInfo) {
            throw new OrderException(ResponseCodeEnum.PARAM_ERROR);
        } else {
            return iOrderInfoServices.getBean(xslOrderInfo.getOrderType()).queryActivitySubDetail(bizParams.getOrderId());
        }
    }
}
