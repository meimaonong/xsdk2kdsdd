package com.art.app.orm.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文章表
 * </p>
 *
 * @author john
 * @since 2019-12-17
 */
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 序列id，uuid生成
     */
    @TableField("sn_id")
    private String snId;
    /**
     * 0:不推荐 1:推荐
     */
    @TableField("is_recommend")
    private Integer isRecommend;
    /**
     * 排序分值
     */
    private Integer rank;
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
     * 0:文章 1:视频
     */
    private Integer type;
    /**
     * 0:h5 1:native
     */
    private Integer mode;
    /**
     * 0:app 1:后台编辑
     */
    private Integer from;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 关键字
     */
    private String keywords;
    /**
     * 视频（标签）类型文章视频url
     */
    @TableField("video_url")
    private String videoUrl;
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
     * 文章内容 (如果是native，存为json结构)
     */
    private String content;
    /**
     * 0:非草稿 1:草稿
     */
    @TableField("is_draft")
    private Integer isDraft;

    @TableField("reject_message")
    private String rejectMessage;
    /**
     * 0:未审核 1:审核中  2:打回 3: 已审核
     */
    private Integer status;
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

    public Integer getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
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

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
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

    public Integer getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Integer isDraft) {
        this.isDraft = isDraft;
    }

    public String getRejectMessage() {
        return rejectMessage;
    }

    public void setRejectMessage(String rejectMessage) {
        this.rejectMessage = rejectMessage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        return "Article{" +
                "id=" + id +
                ", snId=" + snId +
                ", isRecommend=" + isRecommend +
                ", rank=" + rank +
                ", userId=" + userId +
                ", likeNum=" + likeNum +
                ", readNum=" + readNum +
                ", type=" + type +
                ", mode=" + mode +
                ", from=" + from +
                ", title=" + title +
                ", keywords=" + keywords +
                ", videoUrl=" + videoUrl +
                ", thumbUrl=" + thumbUrl +
                ", description=" + description +
                ", contents=" + content +
                ", isDraft=" + isDraft +
                ", rejectMessage=" + rejectMessage +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", delFlag=" + delFlag +
                "}";
    }
}
