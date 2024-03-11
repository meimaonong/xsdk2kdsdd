package com.art.app.web.service;

import com.art.app.common.bean.MatchingBean;
import com.art.app.common.enums.OrderPaymentStatusEnum;
import com.art.app.orm.entity.XslOrderInfo;
import com.art.app.payment.entity.PrePayRequest;
import com.art.app.web.bean.OrderBasicInfo;
import com.art.app.web.bean.request.BizParam;
import com.art.app.web.bean.request.activity.ActivitySubListBizParam;
import com.art.app.web.bean.request.order.OrderRequestVo;
import com.art.app.web.bean.request.order.PrePayBizParam;
import com.art.app.web.bean.response.activity.ActivitySubDetail;
import com.art.app.web.bean.response.activity.ActivitySubList;
import com.art.app.web.bean.response.activity.CommonActivityInfo;
import com.art.app.web.bean.response.order.OrderResult;

import java.util.List;

public interface IOrderInfoService extends MatchingBean<Integer> {

    OrderResult applyOrder(OrderRequestVo<? extends BizParam> requestVo);

    PrePayRequest buildPrePayRequest(XslOrderInfo xslOrderInfo, PrePayBizParam orderBizParams);

    CommonActivityInfo queryCommonActivityInfo(int userId);

    void updateOrderPaymentStatus(XslOrderInfo xslOrderInfo, OrderPaymentStatusEnum fromStatus, OrderPaymentStatusEnum toStatus);

    Integer countActivitySubList(ActivitySubListBizParam bizParams, int userId);

    List<ActivitySubList> queryActivitySubList(ActivitySubListBizParam bizParams, int start, int userId);

    ActivitySubDetail queryActivitySubDetail(String orderId);

    OrderBasicInfo queryOrderBasicInfo(String orderId);
}
