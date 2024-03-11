package com.art.app.web.bean.response;

import com.art.app.common.enums.ComplainReasonEnum;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class ComplainReason {

    private String desc;
    private int reason;

    public ComplainReason(String desc, int reason) {
        this.desc = desc;
        this.reason = reason;
    }

    public static final List<ComplainReason> complains = Lists.newArrayList();

    static {
        for (ComplainReasonEnum value : ComplainReasonEnum.values()) {
            complains.add(new ComplainReason(value.getDesc(), value.getReason()));
        }
    }
}
