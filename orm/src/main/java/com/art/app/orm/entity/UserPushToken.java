package com.art.app.orm.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dragon123
 * @since 2020-05-27
 */
@TableName("user_push_token")
public class UserPushToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 未注册用户的device标示
     */
    @TableField("client_id")
    private String clientId;
    @TableField("user_id")
    private Integer userId;
    /**
     * 0:极光
     */
    private Integer source;
    /**
     * 该渠道的deviceToken
     */
    private String token;
    @TableField("del_flag")
    private Integer delFlag;
    @TableField("created_at")
    private Date createdAt;
    @TableField("updated_at")
    private Date updatedAt;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
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

    @Override
    public String toString() {
        return "UserPushToken{" +
        "id=" + id +
        ", clientId=" + clientId +
        ", userId=" + userId +
        ", source=" + source +
        ", token=" + token +
        ", delFlag=" + delFlag +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        "}";
    }
}
