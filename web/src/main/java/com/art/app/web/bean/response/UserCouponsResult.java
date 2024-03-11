package com.art.app.web.bean.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserCouponsResult {
    /**
     * hasMore : false
     * hasUnRead : false
     * contents : [{"desc":"xxx","msgId":"xxxx","status":0}]
     */

    private boolean hasMore;
    private String desc;
    private List<CouponDetail> contents;

    @Data
    public static class CouponDetail {
        /**
         * type : 0
         * amount : 500
         * detail : 参与平台活动可使用
         * validStarted : xxxxxx
         * validEnded : xxxxx
         */
        private int couponId;
        private int type;
        private int status;
        private double amount;
        private String detail;
        private Date validStarted;
        private Date validEnded;

    }
}
