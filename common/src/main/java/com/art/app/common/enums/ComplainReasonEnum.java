package com.art.app.common.enums;

import lombok.Getter;

@Getter
public enum ComplainReasonEnum {

    REASON_1("作品质量差", 1),
    REASON_2("标题夸张，不实信息", 2),
    REASON_3("有害信息", 3),
    REASON_4("涉黄信息", 4),
    REASON_5("违法信息", 5),
    ;

    private String desc;
    private int reason;

    ComplainReasonEnum(String desc, int reason) {
        this.desc = desc;
        this.reason = reason;
    }

    public static ComplainReasonEnum valueOfReason(int reason) {
        for (ComplainReasonEnum value : ComplainReasonEnum.values()) {
            if (value.getReason() == reason) {
                return value;
            }
        }
        return REASON_1;
    }
}
