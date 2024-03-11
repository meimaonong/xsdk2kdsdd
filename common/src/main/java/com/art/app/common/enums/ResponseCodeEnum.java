package com.art.app.common.enums;

import com.art.app.common.bean.ErrorCode;

public enum ResponseCodeEnum implements ErrorCode {

    SUCCESS(0, "成功"),
    PARAM_ERROR(400, "请检查请求参数"),
    AUTH_FAIL(401, "签名错误"),
    METHOD_ERROR(405, "不支持的http方法"),
    SYSTEM_ERROR(500, "系统错误，请稍后重试"),
    INVALID_PHONE(100000, "无效的手机号码"),
    TOO_FREQUENT_REQUEST(100100, "请稍后再尝试"),
    INVALID_VERIFY_CODE(100200, "无效的验证码"),
    INVALID_TOKEN(100300, "登录已过期，请重新登录"),
    INVALID_USER(100301, "无效用户"),
    DRAFT_FULL(100400, "草稿箱已满"),
    FOCUS_ERROR(100500, "不能关注自己"),
    TIMESTAMP_ERROR(100600, "时间戳异常"),
    TIMESTAMP_MISSING(100601, "缺少时间戳"),
    SIGNATURE_MISSING(100602, "缺少签名"),
    FORBIDDEN_REMOVE_FILE(100700, "无法删除"),
    FORBIDDEN_MODIFY_FILE(100701, "无法编辑"),
    DUPLICATED_NICKNAME(100800, "昵称已被使用"),

    TRAINING_CLASS_INVALID_ERROR(101000, "无效的高研班"),
    TRAINING_CLASS_APPLY_TIME_ERROR(101001, "高研班报名时间错误"),
    TRAINING_CLASS_APPLY_PRICE_ERROR(101002, "高研班价格错误"),
    EXHIBITION_INVALID_ERROR(101003, "无效的艺术展览"),
    EXHIBITION_APPLY_TIME_ERROR(101004, "艺术展览报名时间错误"),
    EXHIBITION_APPLY_PRICE_ERROR(101005, "艺术展览价格错误"),
    SKETCHING_INVALID_ERROR(101006, "无效的活动"),
    SKETCHING_APPLY_TIME_ERROR(101007, "活动报名时间错误"),
    SKETCHING_APPLY_PRICE_ERROR(101008, "活动价格错误"),
    ORDER_LATESET_PAY_TIME_ERROR(101003, "已过最晚付款时间"),

    MEMBER_DESCRIPTION_INVALID_ERROR(102000, "无效的会员信息"),
    MEMBER_DESCRIPTION_APPLY_PRICE_ERROR(102002, "会员价格错误"),

    ;
    private final int code;
    private final String msg;

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ResponseCodeEnum ofCode(int code) {
        for (ResponseCodeEnum value : ResponseCodeEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return SYSTEM_ERROR;
    }

}
