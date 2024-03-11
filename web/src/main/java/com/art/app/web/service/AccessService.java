package com.art.app.web.service;

import com.art.app.orm.entity.AccessLog;
import com.art.app.orm.service.AccessLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AccessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessService.class);

    @Autowired
    private AccessLogService accessLogService;

    @Async
    public void recordAccessLog(AccessLog accessLog) {
        try {
            accessLogService.insert(accessLog);
        } catch (Exception e) {
            LOGGER.error("recordAccessLog content:{}", accessLog, e);
        }
    }
}
