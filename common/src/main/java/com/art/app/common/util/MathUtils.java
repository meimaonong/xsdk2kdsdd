package com.art.app.common.util;

import org.apache.commons.lang.math.NumberUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class MathUtils {

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static final Pattern VERSION_PATTERN =
            Pattern.compile("(([0-9]|([1-9]([0-9]*))).){2}([0-9]|([1-9]([0-9]*)))");

    public static boolean validVersionName(String versionName) {
        return VERSION_PATTERN.matcher(versionName).matches();
    }

    public static boolean validVersionUpgrade(String versionName, String versionDb) {
        if (!validVersionName(versionName) || !validVersionName(versionDb)) {
            return false;
        }
        String[] versions = versionName.split("\\.");
        String[] versionDbs = versionDb.split("\\.");
        if (versions.length != versionDbs.length) {
            return false;
        }
        for (int i = 0; i < versions.length; i++) {
            int v1 = NumberUtils.toInt(versions[i]);
            int v2 = NumberUtils.toInt(versionDbs[i]);
            if (v1 < v2) {
                return true;
            } else if (v1 == v2) {
            } else {
                return false;
            }
        }
        return false;
    }

    public static String format(BigDecimal data) {
        if (data == null) {
            return "0.00";
        }
        DecimalFormat df1 = new DecimalFormat("0.00");
        return df1.format(data);
    }

}
