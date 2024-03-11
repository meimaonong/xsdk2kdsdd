package com.art.app.common.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractTask {

    private static final Logger LOG = LoggerFactory.getLogger("tasklogger");

    protected final static Integer PER_QUERY_NUMBERS = 200;

    public void dealJob(String jobName, AtomicBoolean jobIsDoing) {
        long startTime = System.currentTimeMillis();
        LOG.info("开始处理{}任务", jobName);
        if (!isDoingJob(jobName, jobIsDoing)) {
            try {
                dealJob();
            } catch (Exception e) {
                LOG.error(jobName + "任务执行异常", e);
            } finally {
                releaseJob(jobName, jobIsDoing);
            }
        }
        LOG.info("处理{}任务结束, 耗时->{}", jobName, (System.currentTimeMillis() - startTime));
    }

    protected abstract void dealJob();

    private boolean isDoingJob(String jobName, AtomicBoolean jobIsDoing) {
        if (!jobIsDoing.compareAndSet(false, true)) {
            if (LOG.isInfoEnabled()) {
                LOG.info("上一次处理{}任务还未执行完成", jobName);
            }
            return true;
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info("开始处理{}任务", jobName);
            }
            return false;
        }
    }

    private void releaseJob(String jobName, AtomicBoolean jobIsDoing) {
        if (LOG.isInfoEnabled()) {
            LOG.info("{}任务处理完成", jobName);
        }
        jobIsDoing.set(false);
    }

}
