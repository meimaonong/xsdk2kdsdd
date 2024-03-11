package com.art.app.common.util;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Date;

public class OrderUtils {

    /**
     * 简单的生成订单号规则
     * @param bizCode
     * @return
     */
    public static String createOrderId(String bizCode) {
        String currentDate = DatetimeUtils.formatDatetime(new Date(), DatetimeUtils.YYYYMMDDHHMMSSSSS);
        return String.format("%s%s%s%s",
                bizCode.toUpperCase(),
                currentDate,
                RandomStringUtils.randomNumeric(6),
                RandomStringUtils.randomNumeric(1));
    }
}
