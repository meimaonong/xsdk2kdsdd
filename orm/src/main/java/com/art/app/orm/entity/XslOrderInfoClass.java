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

@TableName("xsl_order_info_class")
@Data
public class XslOrderInfoClass extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4180668013290527605L;

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
    @TableField("class_id")
    private String classId;
    @TableField("class_name")
    private String className;
    @TableField("thumb_url")
    private String thumbUrl;
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    @TableField("class_start_time")
    private Date classStartTime;
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    @TableField("class_end_time")
    private Date classEndTime;
    @TableField("assemble_id")
    private Integer assembleId;
    @TableField("name")
    private String name;
    @TableField("phone")
    private String phone;
    @TableField("remark")
    private String remark;
    @TableField("works")
    private String works;
}
