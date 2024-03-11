package com.art.app.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;

/**
 * base64 file
 */
public class Base64Utils {

    private final static Logger LOGGER = LoggerFactory.getLogger(Base64Utils.class);

    public static String string2Base64(String input) {
        try {
            return Base64.getEncoder().encodeToString(input.getBytes());
        } catch (Exception e) {
            LOGGER.error("string2Base64 convert error:{}", input, e);
        }
        return null;
    }

    public static String base642String(String input) {
        try {
            return new String(Base64.getDecoder().decode(input));
        } catch (Exception e) {
            LOGGER.error("base642String convert error:{}", input, e);
        }
        return null;
    }

    public static String decodeParams(String input) {
        try {
            return base642String(URLDecoder.decode(input, "utf-8")
                    .replaceAll(" +", "+"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }
}
