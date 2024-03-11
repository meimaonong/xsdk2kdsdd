package com.art.app.web.bean.response;

import com.art.app.web.bean.MemberPointsRank;
import lombok.Data;

import java.util.List;

@Data
public class PointsHomeResult {

    /**
     * points : 1111
     * rank : 1
     * hasMore : false
     * rankList : [{"nickName":"xxx","avatar":"xxxx","rank":2,"points":11111}]
     * rule : xxxxx
     * usageRule : xxxxx
     * incentive : [{"imgUrl":"xxxxxxx","name":"xxxxxx","desc":"xxxxxxx"}]
     */

    private int points;
    private int rank;
    private boolean hasMore;
    private String rule;
    private String usageRule;
    private List<MemberPointsRank> rankList;
    private List<IncentiveBean> incentive;

    @Data
    public static class IncentiveBean {
        /**
         * imgUrl : xxxxxxx
         * name : xxxxxx
         * desc : xxxxxxx
         */

        private String imgUrl;
        private String name;
        private String desc;
    }
}
