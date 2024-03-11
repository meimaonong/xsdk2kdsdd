package com.art.app.orm.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName("member_description_info")
@Data
public class MemberDescriptionInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 4829369623890915122L;

    @TableField("member_level")
    private Integer memberLevel; // 会员等级 1普通会员 2高级会员
    @TableField("member_level_version")
    private Integer memberLevelVersion; // 会员等级版本
    @TableField("member_level_name")
    private String memberLevelName;
    @TableField("content")
    private String content; // 会员描述
    @TableField("duration")
    private Integer duration; // 会员持续时间
    @TableField("duration_unit")
    private Integer durationUnit; // 会员持续时间单位 1天 2周 3月 4年
    @TableField("price")
    private BigDecimal price; // 费用
    private BigDecimal promotion; // 促销价
}
