package com.art.app.web.bean.response;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class MessageCategoryResult {
    /**
     * hasMore : false
     * hasUnRead : false
     * contents : [{"desc":"xxx","msgId":"xxxx","status":0}]
     */

    private boolean hasMore;
    private boolean hasUnRead;
    private List<ContentsBean> contents;

    @Data
    public static class ContentsBean {
        /**
         * desc : xxx
         * msgId : xxxx
         * status : 0
         */

        private String title;
        private Map<String, Object> content;
        private String desc;
        private String imgUrl;
        private String msgId;
        private int status;
        private Date createdAt;
        private String detailUrl;
    }
}
