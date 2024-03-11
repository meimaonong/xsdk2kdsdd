package com.art.app.orm.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import java.io.Serializable;

/**
 * <p>
 * 优惠基本信息劵
 * </p>
 *
 * @author dragon123
 * @since 2020-06-27
 */
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 优惠券类型，0:抵用券
     */
    private Integer type;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 抵用券有效期
     */
    @TableField("valid_start_at")
    private Date validStartAt;
    /**
     * 抵用券有效期
     */
    @TableField("valid_end_at")
    private Date validEndAt;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 已派发数
     */
    private Integer dispatched;
    @TableField("created_at")
    private Date createdAt;
    @TableField("updated_at")
    private Date updatedAt;
    @TableField("del_flag")
    private Integer delFlag;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    private String detail;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getValidStartAt() {
        return validStartAt;
    }

    public void setValidStartAt(Date validStartAt) {
        this.validStartAt = validStartAt;
    }

    public Date getValidEndAt() {
        return validEndAt;
    }

    public void setValidEndAt(Date validEndAt) {
        this.validEndAt = validEndAt;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getDispatched() {
        return dispatched;
    }

    public void setDispatched(Integer dispatched) {
        this.dispatched = dispatched;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "Coupon{" +
        "id=" + id +
        ", type=" + type +
        ", amount=" + amount +
        ", validStartAt=" + validStartAt +
        ", validEndAt=" + validEndAt +
        ", stock=" + stock +
        ", dispatched=" + dispatched +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", delFlag=" + delFlag +
        "}";
    }
}
