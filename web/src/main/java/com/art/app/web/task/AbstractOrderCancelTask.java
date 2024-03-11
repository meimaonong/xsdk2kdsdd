package com.art.app.web.task;

import com.art.app.common.Constants;
import com.art.app.common.enums.OrderPaymentStatusEnum;
import com.art.app.common.enums.OrderStatusEnum;
import com.art.app.common.enums.PaymentStatusEnum;
import com.art.app.common.task.AbstractTask;
import com.art.app.common.util.DatetimeUtils;
import com.art.app.payment.entity.QueryPayResultRequest;
import com.art.app.payment.entity.QueryPayResultResponse;
import com.art.app.payment.service.IPaymentService;
import com.art.app.web.bean.OrderCancelBasicInfo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class AbstractOrderCancelTask extends AbstractTask {

    private static final Logger log = LoggerFactory.getLogger("tasklogger");

    @Resource
    private IPaymentService iPaymentService;

    @Override
    protected void dealJob() {
        int tmpNumbers = 0;
        Integer id = 0;
        List<Integer> statusEnums = Arrays.asList(OrderPaymentStatusEnum.NOT_PAID.getStatus(),
                OrderPaymentStatusEnum.PAYING.getStatus());
        Date nowDate = DatetimeUtils.now();
        Date maxPaymentTime = DatetimeUtils.addMinute(nowDate, -Constants.MAX_PAYMENT_TIME);
        Integer numbers = queryNumbersByStatus(statusEnums, maxPaymentTime);
        log.info("订单取消任务，需要处理的数量 -> {}", numbers);
        while (null != numbers && tmpNumbers < numbers) {
            try {
                List<OrderCancelBasicInfo> orderInfos = queryOrderCancelBasicInfo(statusEnums, id, PER_QUERY_NUMBERS, maxPaymentTime);
                if (!CollectionUtils.isEmpty(orderInfos)) {
                    for (OrderCancelBasicInfo basicInfo : orderInfos) {
                        try {
                            dealOrderInfo(basicInfo, nowDate);
                        } catch (Exception e) {
                            log.error("订单取消任务异常 -> " + basicInfo.getOrderId(), e);
                        } finally {
                            id = id < basicInfo.getId() ? basicInfo.getId() : id;
                        }
                    }
                } else {
                    break;
                }
            } catch (Exception e) {
                log.error("订单取消任务异常", e);
            } finally {
                tmpNumbers += PER_QUERY_NUMBERS;
            }
        }
    }

    private void dealOrderInfo(OrderCancelBasicInfo basicInfo, Date nowDate) {
        if (OrderPaymentStatusEnum.NOT_PAID.getStatus().equals(basicInfo.getPaymentStatus())) {
            // 如果1小时(这里放宽一点时间)内还是未支付，更新为取消状态
            Integer minutes = Constants.MAX_PAYMENT_TIME + 10;
            if (!DatetimeUtils.addMinute(nowDate, -minutes).before(basicInfo.getCreatedAt())) {
                updateOrderInfo(basicInfo.getId(), OrderStatusEnum.APPLY_FAIL.getType(), OrderPaymentStatusEnum.CANCEL.getStatus());
            }
        } else if (PaymentStatusEnum.PAYING.getStatus().equals(basicInfo.getPaymentStatus())) {
            // 超过2小时(这里放宽一点时间)还是支付中状态，去支付模块查询支付结果，更新状态
            Integer minutes = Constants.MAX_PAYMENT_TIME * 2 + 10;
            if (!DatetimeUtils.addMinute(nowDate, -minutes).before(basicInfo.getCreatedAt())) {
                QueryPayResultRequest payResultRequest = new QueryPayResultRequest();
                payResultRequest.setOrderId(basicInfo.getOrderId());
                QueryPayResultResponse payResultResponse = iPaymentService.queryPayResult(payResultRequest, true);
                if (PaymentStatusEnum.PAY_SUCCESS.getStatus().equals(payResultResponse.getResult())) {
                    updateOrderInfo(basicInfo.getId(), OrderStatusEnum.APPLY_SUCCESS.getType(), OrderPaymentStatusEnum.SUCCESS.getStatus());
                } else {
                    updateOrderInfo(basicInfo.getId(), OrderStatusEnum.APPLY_FAIL.getType(), OrderPaymentStatusEnum.CANCEL.getStatus());
                }
            }
        }
    }

    protected abstract void updateOrderInfo(Integer id, Integer orderStatus, Integer orderPaymentStatus);

    protected abstract List<OrderCancelBasicInfo> queryOrderCancelBasicInfo(List<Integer> statusEnums, Integer id, Integer limit, Date maxPaymentTime);

    protected abstract Integer queryNumbersByStatus(List<Integer> statusEnums, Date maxPaymentTime);
}
