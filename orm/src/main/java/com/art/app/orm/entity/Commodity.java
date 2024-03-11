package com.art.app.orm.entity;

import com.baomidou.mybatisplus.annotations.TableField;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 精选（推荐商品）文章表
 * </p>
 *
 * @author john
 * @since 2019-12-17
 */
public class Commodity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id，不规则唯一id（防爬）
     */
    private Integer id;
    /**
     * uuid
     */
    @TableField("sn_id")
    private String snId;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 点赞次数
     */
    @TableField("like_num")
    private Integer likeNum;
    /**
     * 阅读次数
     */
    @TableField("read_num")
    private Integer readNum;
    /**
     * 0:书籍 1:画材
     */
    private Integer type;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 文章封面
     */
    @TableField("thumb_url")
    private String thumbUrl;
    /**
     * 文章描述
     */
    private String description;
    /**
     * 文章内容
     */
    private String content;
    @TableField("created_at")
    private Date createdAt;
    @TableField("updated_at")
    private Date updatedAt;
    @TableField("del_flag")
    private Integer delFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSnId() {
        return snId;
    }

    public void setSnId(String snId) {
        this.snId = snId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        return "Commodity{" +
                "id=" + id +
                ", snId='" + snId + '\'' +
                ", userId=" + userId +
                ", likeNum=" + likeNum +
                ", readNum=" + readNum +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", delFlag=" + delFlag +
                '}';
    }
}
