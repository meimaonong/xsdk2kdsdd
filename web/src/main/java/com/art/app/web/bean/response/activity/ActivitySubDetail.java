package com.art.app.web.bean.response.activity;

import com.art.app.common.serializer.DateDeserializer;
import com.art.app.common.serializer.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ActivitySubDetail {

    private Integer status; // 0:进行中，1:已完成， 2:已过期
    private String orderId; // 业务订单号
    private String title; //课程描述
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date createdAt;
    private String imgUrl;
    private String resourceId;
    private Integer payType; //0:支付宝，1:微信
    private BigDecimal price; // 应付金额
    private BigDecimal payment; // 实付金额

}
