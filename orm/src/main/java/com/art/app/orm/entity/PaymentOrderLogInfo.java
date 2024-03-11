package com.art.app.orm.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("payment_order_log_info")
@Data
public class PaymentOrderLogInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1732207855475856936L;

    @TableField("log_type")
    private Integer logType; // 日志类型
    @TableField("art_order_id")
    private String artOrderId; // 订单号
    @TableField("payment_order_id")
    private String paymentOrderId; // 支付订单号
    @TableField("out_order_id")
    private String outOrderId; // 外部订单号
    @TableField("request_status")
    private Integer requestStatus; // 接口调用结果 1成功 2失败
    @TableField("cost_time")
    private Long costTime; // 接口耗时
    @TableField("request_log")
    private String requestLog; // 请求参数
    @TableField("response_log")
    private String responseLog; // 响应参数
}
