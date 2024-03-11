package com.art.app.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DatetimeUtils {

    public final static String YYYY_MM_DD = "yyyy-MM-dd";

    public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public final static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    public final static String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

    public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    private DatetimeUtils() {
    }

    public static Date getStartOfToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.of(ZoneId.SHORT_IDS.get("CTT"))).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.of(ZoneId.SHORT_IDS.get("CTT"))).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.of(ZoneId.SHORT_IDS.get("CTT"))).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.of(ZoneId.SHORT_IDS.get("CTT"))).toLocalDateTime();
    }

    public static LocalDateTime getNow() {
        return Instant.ofEpochMilli(System.currentTimeMillis())
                .atZone(ZoneId.of(ZoneId.SHORT_IDS.get("CTT"))).toLocalDateTime();
    }

    public static Date now(String zoneId) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return Date.from(localDateTime.atZone(ZoneId.of(zoneId)).toInstant());
    }

    public static Date now() {
        return new Date();
//        LocalDateTime localDateTime = LocalDateTime.now();
//        return Date.from(localDateTime.atZone(ZoneId.of(ZoneId.SHORT_IDS.get("CTT"))).toInstant());
    }

    public static String formatDatetime(Date date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = asLocalDateTime(date);
        return formatter.format(localDateTime);
    }

    public static String formatDatetime(Date date) {
        return formatDatetime(date, YYYY_MM_DD_HH_MM_SS);
    }

    public static Date parseDatetime(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(dateStr));
        return asDate(localDateTime);
    }

    public static Date parseDatetime(String dateStr) {
        return parseDatetime(dateStr, YYYY_MM_DD_HH_MM_SS);
    }

    public static String formatDate(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD);
        LocalDate localDate = asLocalDate(date);
        return formatter.format(localDate);
    }

    public static Date parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD);
        LocalDate localDate = LocalDate.from(formatter.parse(dateStr));
        return asDate(localDate);
    }

    public static Date addMinute(Date date, long minutes) {
        LocalDateTime localDateTime = asLocalDateTime(date);
        localDateTime = localDateTime.plusMinutes(minutes);
        return asDate(localDateTime);
    }

    public static Date addDay(Date date, long days) {
        LocalDateTime localDateTime = asLocalDateTime(date);
        localDateTime = localDateTime.plusDays(days);
        return asDate(localDateTime);
    }

    public static void main(String[] args) {
        System.out.println(new Date());
        System.out.println(now(ZoneId.SHORT_IDS.get("CTT")));
        System.out.println(formatDatetime(new Date()));
        System.out.println(formatDatetime(now()));
        System.out.println(parseDatetime(formatDatetime(now())));
    }

}
