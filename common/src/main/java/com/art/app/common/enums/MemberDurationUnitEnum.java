package com.art.app.common.enums;

import com.art.app.common.util.DatetimeUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Slf4j
public enum MemberDurationUnitEnum {
    // 1天 2周 3月 4年
    UNKNOWN(0, "未知"),
    DAY(1, "天"),
    WEEK(2, "周"),
    MONTH(3, "月"),
    YEAR(4, "年"),
    ;

    private int type;
    private String name;

    MemberDurationUnitEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static MemberDurationUnitEnum ofType(int type) {
        for (MemberDurationUnitEnum value : MemberDurationUnitEnum.values()) {
            if (type == value.getType()) {
                return value;
            }
        }
        return UNKNOWN;
    }

    public static Date convertDuration(Date currentTime, int duration, int durationUnit) {
        LocalDate date = null == currentTime ? LocalDate.now() : DatetimeUtils.asLocalDate(currentTime);
        if (durationUnit == MemberDurationUnitEnum.DAY.getType()) {
            date = date.plusDays(duration);
        } else if (durationUnit == MemberDurationUnitEnum.WEEK.getType()) {
            date = date.plusWeeks(duration);
        } else if (durationUnit == MemberDurationUnitEnum.MONTH.getType()) {
            date = date.plusMonths(duration);
        } else if (durationUnit == MemberDurationUnitEnum.YEAR.getType()) {
            date = date.plusYears(duration);
        } else {
            log.info("unknown durationUnit");
        }
        return DatetimeUtils.asDate(date);
    }
}
