package com.art.app.web.bean.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CourseResult {

    private boolean hasMore = false;
    private List<CourseInfo> contents;

    @Data
    public static class CourseInfo {
        private int userId;
        private String avatar;
        private String author;
        private String title;
        private Date createdAt;
        private int viewNum;
        private int type;
        private String imgUrl;
        private String resourceId;
        private String detailUrl;
        private String description;
    }
}
