package com.art.app.common.serializer;

import com.art.app.common.util.DatetimeUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Date;

@Slf4j
public class DateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String date = jp.getText();
        if (date == null || date.trim().length() == 0) {
            return null;
        }
        try {
            return DatetimeUtils.parseDate(date);
        } catch (Exception e) {
            log.error("日期解析出错: " + date, e);
        }
        return null;
    }

}
