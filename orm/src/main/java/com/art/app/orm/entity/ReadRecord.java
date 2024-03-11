package com.art.app.orm.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 浏览记录表
 * </p>
 *
 * @author dragon123
 * @since 2019-12-17
 */
@TableName("read_record")
public class ReadRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 访问资源的id
     */
    @TableField("resource_id")
    private String resourceId;
    /**
     * 用户id，未登录浏览为0
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 0:文章 1: 精选 2:培训班 3:教程
     */
    private Integer type;
    @TableField("created_at")
    private Date createdAt;
    @TableField("updated_at")
    private Date updatedAt;
    @TableField("del_flag")
    private Integer delFlag;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
        return "ReadRecord{" +
        "id=" + id +
        ", resourceId=" + resourceId +
        ", userId=" + userId +
        ", type=" + type +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", delFlag=" + delFlag +
        "}";
    }
}
