package com.art.app.web.bean.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TrainingResult {

    private boolean hasMore = false;
    private List<Training> contents;

    @Data
    public static class Training {
        private String title;
        private int status;
        private String description;
        private Date createdAt;
        private int viewNum;
        private String imgUrl;
        private String resourceId;
        private String detailUrl;
        private int userId;
        private String avatar;
        private String author;
    }


}
