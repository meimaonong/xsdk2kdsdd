package com.art.app.orm.entity;

import com.art.app.common.serializer.DateTimeDeserializer;
import com.art.app.common.serializer.DateTimeSerializer;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("xsl_order_info_exhibition")
@Data
public class XslOrderInfoExhibition extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3817194303608322752L;

    @TableField("user_id")
    private Integer userId;
    @TableField("order_id")
    private String orderId;
    @TableField("order_status")
    private Integer orderStatus;
    @TableField("payment_status")
    private Integer paymentStatus;
    @TableField("payment_type")
    private Integer paymentType;
    @TableField("amount")
    private BigDecimal amount;
    @TableField("pay_amount")
    private BigDecimal payAmount;
    @TableField("exhibition_id")
    private String exhibitionId;
    @TableField("exhibition_name")
    private String exhibitionName;
    @TableField("thumb_url")
    private String thumbUrl;
    @TableField("assemble_id")
    private Integer assembleId;
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    @TableField("exhibition_start_time")
    private Date exhibitionStartTime;
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    @TableField("exhibition_end_time")
    private Date exhibitionEndTime;
    @TableField("name")
    private String name;
    @TableField("phone")
    private String phone;
    @TableField("remark")
    private String remark;
    @TableField("works")
    private String works;
}
