package com.art.app.web.bean.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommodityResult extends HomePageResult {

    private List<CommodityInfo> contents;

    @Data
    public static class CommodityInfo {
        private int type;
        private String title;
        private int userId;
        private String author;
        private String avatar;
        private Date createdAt;
        private int viewNum;
        private int likeNum;
        private String imgUrl;
        private String resourceId;
        private boolean like = false;
        private boolean focus = false;
        private String detailUrl;
        private String description;
    }
}
