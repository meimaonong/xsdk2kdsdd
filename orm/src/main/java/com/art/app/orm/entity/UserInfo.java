package com.art.app.orm.entity;

import com.art.app.common.serializer.DateDeserializer;
import com.art.app.common.serializer.DateSerializer;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author john
 * @since 2019-12-17
 */
@TableName("user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户信息id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 0:普通会员  1:高级会员
     */
    private Integer degree;

    @TableField("expire_time")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date expireTime;
    /**
     * 用户名字首字母
     */
    private String letter;
    /**
     * 0:普通用户  1:艺术家 
     */
    @TableField("is_artist")
    private Integer isArtist;
    /**
     * 0:app从未登录  1:app曾经登录过
     */
    @TableField("is_login")
    private Integer isLogin;
    /**
     * 个人空间背景图
     */
    @TableField("space_img_url")
    private String spaceImgUrl;
    /**
     * 用户真实姓名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 用户昵称
     */
    @TableField("nick_name")
    private String nickName;
    /**
     * 头像图片地址
     */
    private String avatar;
    /**
     * 座右铭
     */
    private String motto;
    /**
     * 用户简介（空间可用）
     */
    private String intro;
    /**
     * 邀请人数
     */
    @TableField("invite_num")
    private Integer inviteNum;
    /**
     * 浏览人数（人气）
     */
    @TableField("view_num")
    private Integer viewNum;
    /**
     * 关注人数
     */
    @TableField("focus_num")
    private Integer focusNum;
    /**
     * 粉丝数量
     */
    @TableField("fans_num")
    private Integer fansNum;
    @TableField("created_at")
    private Date createdAt;
    @TableField("updated_at")
    private Date updatedAt;
    @TableField("del_flag")
    private Integer delFlag;

    private Integer points;

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public Integer getIsArtist() {
        return isArtist;
    }

    public void setIsArtist(Integer isArtist) {
        this.isArtist = isArtist;
    }

    public Integer getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Integer isLogin) {
        this.isLogin = isLogin;
    }

    public String getSpaceImgUrl() {
        return spaceImgUrl;
    }

    public void setSpaceImgUrl(String spaceImgUrl) {
        this.spaceImgUrl = spaceImgUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Integer getInviteNum() {
        return inviteNum;
    }

    public void setInviteNum(Integer inviteNum) {
        this.inviteNum = inviteNum;
    }

    public Integer getViewNum() {
        return viewNum;
    }

    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }

    public Integer getFocusNum() {
        return focusNum;
    }

    public void setFocusNum(Integer focusNum) {
        this.focusNum = focusNum;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
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

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", userId=" + userId +
                ", degree=" + degree +
                ", letter='" + letter + '\'' +
                ", isArtist=" + isArtist +
                ", isLogin=" + isLogin +
                ", spaceImgUrl='" + spaceImgUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", motto='" + motto + '\'' +
                ", intro='" + intro + '\'' +
                ", inviteNum=" + inviteNum +
                ", viewNum=" + viewNum +
                ", focusNum=" + focusNum +
                ", fansNum=" + fansNum +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", delFlag=" + delFlag +
                '}';
    }
}
