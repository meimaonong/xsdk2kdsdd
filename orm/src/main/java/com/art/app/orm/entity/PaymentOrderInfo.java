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

@TableName("payment_order_info")
@Data
public class PaymentOrderInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -621626063565284628L;

    @TableField("art_order_id")
    private String artOrderId; // 订单号
    @TableField("art_order_type")
    private Integer artOrderType; // 订单类型 1高研班报名 2购买会员
    @TableField("payment_order_id")
    private String paymentOrderId; // 支付订单号
    @TableField("payment_type")
    private Integer paymentType; // 支付类型 1-支付宝 2-微信
    @TableField("payment_status")
    private Integer paymentStatus; // 支付状态 0未支付 1支付中 2支付成功 3支付失败
    @TableField("payment_status_remark")
    private String paymentStatusRemark; // 支付状态备注
    @TableField("total_amount")
    private BigDecimal totalAmount; // 支付金额
    @TableField("subject")
    private String subject; // 支付主题/关键字
    @TableField("time_expire")
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private Date timeExpire; // 支付超时时间
    @TableField("out_order_id")
    private String outOrderId; // 外部订单号(支付宝或者微信订单号)
    @TableField("pay_success_time")
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private Date paySuccessTime; // 支付成功时间
    @TableField("notify_url")
    private String notifyUrl; // 支付结果回调地址
    @TableField("order_string")
    private String orderString; // 唤醒app支付参数
    @TableField("extend_info")
    private String extendInfo; // 附加信息，记录交易时快照信息，防止退款失败
    @TableField("remark")
    private String remark; // 备注信息

}
