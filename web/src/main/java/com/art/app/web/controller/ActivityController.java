package com.art.app.web.controller;

import com.art.app.common.enums.ResponseCodeEnum;
import com.art.app.payment.annotation.RecordLog;
import com.art.app.web.annotation.LoginRequired;
import com.art.app.web.annotation.SignaturePass;
import com.art.app.web.bean.OrderException;
import com.art.app.web.bean.request.activity.ActivityListBizParam;
import com.art.app.web.bean.request.activity.ActivitySubDetailBizParam;
import com.art.app.web.bean.request.activity.ActivitySubListBizParam;
import com.art.app.web.bean.request.order.OrderRequestVo;
import com.art.app.web.bean.response.ResponseVo;
import com.art.app.web.bean.response.XslList;
import com.art.app.web.bean.response.activity.ActivityList;
import com.art.app.web.bean.response.activity.ActivitySubDetail;
import com.art.app.web.bean.response.activity.ActivitySubList;
import com.art.app.web.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/activities")
@Slf4j
public class ActivityController {

    @Resource
    private ActivityService activityService;

    /**
     * 我的活动列表页
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("/list")
    @LoginRequired
    @RecordLog
    public ResponseVo queryActivityList(@RequestBody OrderRequestVo<ActivityListBizParam> requestVo) {
        XslList<ActivityList> result = activityService.queryActivityList(requestVo);
        return ResponseVo.successData(result);
    }

    /**
     * 我的活动分类列表页
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("/sublist")
    @LoginRequired
    @RecordLog
    public ResponseVo queryActivitySubList(@RequestBody OrderRequestVo<ActivitySubListBizParam> requestVo) {
        ActivitySubListBizParam bizParams = requestVo.getOrderBizParams(ActivitySubListBizParam.class);
        if (null == bizParams || null == bizParams.getType()) {
            throw new OrderException(ResponseCodeEnum.PARAM_ERROR);
        } else {
            XslList<ActivitySubList> result = activityService.queryActivitySubList(requestVo);
            return ResponseVo.successData(result);
        }
    }

    /**
     * 我的活动分类详情页
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("/sublist/detail")
    @LoginRequired
    @RecordLog
    @SignaturePass
    public ResponseVo queryActivitySubDetail(@RequestBody OrderRequestVo<ActivitySubDetailBizParam> requestVo) {
        ActivitySubDetailBizParam bizParams = requestVo.getOrderBizParams(ActivitySubDetailBizParam.class);
        if (null == bizParams
                || StringUtils.isEmpty(bizParams.getOrderId())) {
            throw new OrderException(ResponseCodeEnum.PARAM_ERROR);
        } else {
            ActivitySubDetail result = activityService.queryActivitySubDetail(requestVo);
            return ResponseVo.successData(result);
        }
    }

}
