package com.art.app.orm.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("payment_type_manage")
@Data
public class PaymentTypeManage extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1456506679406045136L;

    @TableField("payment_type")
    private Integer paymentType; // 支付类型 1-支付宝 2-微信
    @TableField("payment_type_status")
    private Integer paymentTypeStatus; // 支付类型状态 1-打开 2-关闭
    @TableField("app_id")
    private String appId; // 开放平台appId
    @TableField("extend_info")
    private String extendInfo; // 附加信息
    @TableField("remark")
    private String remark; // 备注信息
}
