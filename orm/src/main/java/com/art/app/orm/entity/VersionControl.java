package com.art.app.orm.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dragon123
 * @since 2019-12-19
 */
@TableName("version_control")
public class VersionControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("os")
    private Integer os;

    /**
     * 当前版本号
     */
    @TableField("version_name")
    private String versionName;
    /**
     * 待升级版本号
     */
    @TableField("update_version_code")
    private Integer updateVersionCode;
    /**
     * 待升级版本名
     */
    @TableField("update_version_name")
    private String updateVersionName;
    /**
     * 是否强制升级，0:否，1:是
     */
    @TableField("force_update")
    private Integer forceUpdate;
    /**
     * 升级说明
     */
    @TableField("update_log")
    private String updateLog;
    /**
     * 待升级版本下载地址
     */
    @TableField("update_version_url")
    private String updateVersionUrl;
    /**
     * 待升级版本图标地址
     */
    @TableField("update_version_icon")
    private String updateVersionIcon;
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

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getUpdateVersionCode() {
        return updateVersionCode;
    }

    public void setUpdateVersionCode(Integer updateVersionCode) {
        this.updateVersionCode = updateVersionCode;
    }

    public String getUpdateVersionName() {
        return updateVersionName;
    }

    public void setUpdateVersionName(String updateVersionName) {
        this.updateVersionName = updateVersionName;
    }

    public Integer getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Integer forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public String getUpdateVersionUrl() {
        return updateVersionUrl;
    }

    public void setUpdateVersionUrl(String updateVersionUrl) {
        this.updateVersionUrl = updateVersionUrl;
    }

    public String getUpdateVersionIcon() {
        return updateVersionIcon;
    }

    public void setUpdateVersionIcon(String updateVersionIcon) {
        this.updateVersionIcon = updateVersionIcon;
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
        return "VersionControl{" +
        "id=" + id +
        ", versionName=" + versionName +
        ", updateVersionCode=" + updateVersionCode +
        ", updateVersionName=" + updateVersionName +
        ", forceUpdate=" + forceUpdate +
        ", updateLog=" + updateLog +
        ", updateVersionUrl=" + updateVersionUrl +
        ", updateVersionIcon=" + updateVersionIcon +
        ", delFlag=" + delFlag +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        "}";
    }

    public Integer getOs() {
        return os;
    }

    public void setOs(Integer os) {
        this.os = os;
    }
}
