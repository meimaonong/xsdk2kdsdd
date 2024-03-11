package com.art.app.common.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

	@Override
	public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		if(p == null || p.getText() == null || p.getText().trim().length() <=0){
			return null;
		}
		if (StringUtils.isBlank(p.getText()) || "-".equals(p.getText())) {
			return null;
		}
		return LocalDateTime.parse(p.getText(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

}
