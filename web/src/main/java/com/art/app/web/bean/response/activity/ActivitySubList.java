package com.art.app.web.bean.response.activity;

import com.art.app.common.serializer.DateTimeDeserializer;
import com.art.app.common.serializer.DateTimeSerializer;
import com.art.app.web.bean.response.HomePageResult;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

@Data
public class ActivitySubList extends HomePageResult {

    private Integer status; // 0:进行中，1:已完成， 2:已过期
    private String orderId;
    private String title;
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private Date createdAt;
    private String imgUrl;
    private String resourceId;

}
