package com.art.app.web.bean.response;

import lombok.Data;

@Data
public class MessageHomeResult {
    /**
     * hasUnRead : true
     * content : {"title":"标题","desc":"描述","iconUrl":"","type":1}
     */

    private boolean hasUnRead;
    private ContentBean content;


    @Data
    public static class ContentBean {
        /**
         * title : 标题
         * desc : 描述
         * iconUrl :
         * type : 1
         */

        private String title;
        private String desc;
        private String iconUrl;
        private int type;

    }
}
