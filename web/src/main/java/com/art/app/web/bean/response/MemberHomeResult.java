package com.art.app.web.bean.response;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class MemberHomeResult {
    /**
     * nickName : xxxxx
     * avatar : xxxxx
     * rule : xxxxxxxx
     * status : 0
     * memberBenefits : {"normal":{"desc":"xxx","promotion":999,"price":1500},"senior":{"desc":"xxx","promotion":1299,"price":2500}}
     * videoCourses : [{"userId":1,"avatar":null,"author":"弄眉毛","title":"课程描述","createdAt":"2020-01-05T16:41:07.000+0000","viewNum":0,"imgUrl":"","type":1,"resourceId":"d37078a0-2f64-11ea-827c-317b7067c2e3","detailUrl":"xxxxx"}]
     */

    private String nickName;
    private String avatar;
    private String rule;
    private int status;
    private int level;
    private Date validDateEnd;
    private MemberBenefitsBean memberBenefits;
    private List<VideoInfo> videoCourses;

    @Data
    public static class MemberBenefitsBean {
        /**
         * normal : {"desc":"xxx","promotion":999,"price":1500}
         * senior : {"desc":"xxx","promotion":1299,"price":2500}
         */

        private BenefitBean normal;
        private BenefitBean senior;

        @Data
        public static class BenefitBean {
            /**
             * desc : xxx
             * promotion : 999
             * price : 1500
             */

            private List<Map<String, Object>> desc;
            private String promotion;
            private String price;
        }

    }
}
