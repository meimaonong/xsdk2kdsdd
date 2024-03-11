package com.art.app.orm.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName("xsl_order_info_member")
@Data
public class XslOrderInfoMember extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7481601570251851696L;

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
    @TableField("member_level")
    private Integer memberLevel;
    @TableField("member_level_version")
    private Integer memberLevelVersion;
    @TableField("remark")
    private String remark;
}
