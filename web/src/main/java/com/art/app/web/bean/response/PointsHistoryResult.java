package com.art.app.web.bean.response;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PointsHistoryResult {

    private boolean hasMore;
    private List<PointsHistoryBean> contents = Lists.newArrayList();

    @Data
    public static class PointsHistoryBean {

        /**
         * type : 0
         * desc : xxxxx
         * createdAt : 2019-12-20T06:00:00.000+0000
         * pointsLeft : 2112
         */

        private int type;
        private String desc;
        private int point;
        private Date createdAt;
        private int pointsLeft;
    }
}
