package com.art.app.payment.service;

import com.art.app.common.Constants;
import com.art.app.payment.task.PayResultTask;
import com.art.app.payment.task.RefundResultTask;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class PaymentTaskService {

    @Resource
    private PayResultTask payResultTask;
    @Resource
    private RefundResultTask refundResultTask;

    private final AtomicBoolean payResultJob = new AtomicBoolean(false);

    private final AtomicBoolean refundResultJob = new AtomicBoolean(false);

    @Scheduled(cron = "0/10 * * * * ?")
    public void payResultTask() {
        String uuid = UUID.randomUUID().toString();
        MDC.put(Constants.MDC_ID, uuid);
        try {
            payResultTask.dealJob("pay-result", payResultJob);
        } finally {
            MDC.remove(Constants.MDC_ID);
        }
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void refundResultTask() {
        String uuid = UUID.randomUUID().toString();
        MDC.put(Constants.MDC_ID, uuid);
        try {
            refundResultTask.dealJob("refund-result", refundResultJob);
        } finally {
            MDC.remove(Constants.MDC_ID);
        }
    }
}
