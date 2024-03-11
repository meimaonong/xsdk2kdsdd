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

@TableName("payment_refund_order_info")
@Data
public class PaymentRefundOrderInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1607996753627450645L;

    @TableField("art_order_id")
    private String artOrderId; // 订单号
    @TableField("art_order_type")
    private Integer artOrderType; // 订单类型
    @TableField("payment_order_id")
    private String paymentOrderId; // 支付订单号
    @TableField("refund_order_id")
    private String refundOrderId; // 退款订单号
    @TableField("payment_type")
    private Integer paymentType; // 退款类型 1-支付宝 2-微信
    @TableField("refund_status")
    private Integer refundStatus; // 退款状态 0退款中 1退款成功 2退款失败
    @TableField("refund_status_remark")
    private String refundStatusRemark; // 退款状态备注
    @TableField("refund_amount")
    private BigDecimal refundAmount; // 退款金额
    @TableField("refund_reason")
    private String refundReason; // 退款原因
    @TableField("out_order_id")
    private String outOrderId; // 外部订单号(支付宝或者微信订单号)
    @TableField("refund_success_time")
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private Date refundSuccessTime; // 退款成功时间
    @TableField("notify_url")
    private String notifyUrl; // 退款结果回调地址
    @TableField("remark")
    private String remark; // 备注信息
}
