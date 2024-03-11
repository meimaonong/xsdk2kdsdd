package com.art.app.web.service;

import com.art.app.common.Constants;
import com.art.app.payment.task.RefundResultTask;
import com.art.app.web.task.impl.ClassOrderCancelTask;
import com.art.app.web.task.impl.ExhibitionOrderCancelTask;
import com.art.app.web.task.impl.MemberOrderCancelTask;
import com.art.app.web.task.impl.SketchingOrderCancelTask;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class OrderTaskService {

    @Resource
    private ClassOrderCancelTask classOrderCancelTask;
    @Resource
    private MemberOrderCancelTask memberOrderCancelTask;
    @Resource
    private ExhibitionOrderCancelTask exhibitionOrderCancelTask;
    @Resource
    private SketchingOrderCancelTask sketchingOrderCancelTask;

    private final AtomicBoolean classOrderCancelTaskJob = new AtomicBoolean(false);

    private final AtomicBoolean memberOrderCancelTaskJob = new AtomicBoolean(false);

    private final AtomicBoolean exhibitionOrderCancelTaskJob = new AtomicBoolean(false);

    private final AtomicBoolean sketchingOrderCancelTaskJob = new AtomicBoolean(false);

    @Scheduled(cron = "0/10 * * * * ?")
    public void classOrderCancelTask() {
        String uuid = UUID.randomUUID().toString();
        MDC.put(Constants.MDC_ID, uuid);
        try {
            classOrderCancelTask.dealJob("classOrderCancelTask", classOrderCancelTaskJob);
        } finally {
            MDC.remove(Constants.MDC_ID);
        }
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void memberOrderCancelTask() {
        String uuid = UUID.randomUUID().toString();
        MDC.put(Constants.MDC_ID, uuid);
        try {
            memberOrderCancelTask.dealJob("memberOrderCancelTask", memberOrderCancelTaskJob);
        } finally {
            MDC.remove(Constants.MDC_ID);
        }
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void exhibitionOrderCancelTask() {
        String uuid = UUID.randomUUID().toString();
        MDC.put(Constants.MDC_ID, uuid);
        try {
            exhibitionOrderCancelTask.dealJob("exhibitionOrderCancelTask", exhibitionOrderCancelTaskJob);
        } finally {
            MDC.remove(Constants.MDC_ID);
        }
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void sketchingOrderCancelTask() {
        String uuid = UUID.randomUUID().toString();
        MDC.put(Constants.MDC_ID, uuid);
        try {
            sketchingOrderCancelTask.dealJob("sketchingOrderCancelTask", sketchingOrderCancelTaskJob);
        } finally {
            MDC.remove(Constants.MDC_ID);
        }
    }
}
