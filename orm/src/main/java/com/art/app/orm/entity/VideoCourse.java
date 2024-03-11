package com.art.app.orm.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 视频课程表
 * </p>
 *
 * @author dragon123
 * @since 2020-05-30
 */
@TableName("video_course")
public class VideoCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 视频课程id
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 阅读次数
     */
    @TableField("read_num")
    private Integer readNum;
    /**
     * 点赞次数
     */
    @TableField("like_num")
    private Integer likeNum;
    /**
     * 课程标题
     */
    private String title;
    /**
     * 封面地址
     */
    @TableField("thumb_url")
    private String thumbUrl;
    /**
     * 视频地址
     */
    @TableField("video_url")
    private String videoUrl;
    /**
     * 课程描述
     */
    private String description;
    /**
     * 视频课程内容
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

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
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
        return "VideoCourse{" +
        "id=" + id +
        ", snId=" + snId +
        ", userId=" + userId +
        ", readNum=" + readNum +
        ", likeNum=" + likeNum +
        ", title=" + title +
        ", thumbUrl=" + thumbUrl +
        ", videoUrl=" + videoUrl +
        ", description=" + description +
        ", content=" + content +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", delFlag=" + delFlag +
        "}";
    }
}
