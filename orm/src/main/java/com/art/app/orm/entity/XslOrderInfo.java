package com.art.app.orm.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class XslOrderInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3919420070000528098L;

    @TableField("user_id")
    private Integer userId;
    @TableField("order_id")
    private String orderId;
    @TableField("order_type")
    private Integer orderType;
    @TableField("amount")
    private BigDecimal amount;
    @TableField("remark")
    private String remark;
}
