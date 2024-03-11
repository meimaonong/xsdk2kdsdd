package com.art.app.payment.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class JackJsonUtil {
	private static final Logger LOG = LoggerFactory.getLogger(JackJsonUtil.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//				.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
				.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true).registerModule(new JavaTimeModule());
	}

	public static JsonNode readTree(String content) {
		try {
			return objectMapper.readTree(content);
		} catch (Exception e) {
			LOG.error("JackJsonUtil.readTree error: ", e);
		}
		return null;
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public static <T> T convertValue(JsonNode node, TypeReference<T> toValueTypeRef) {
		try {
			return objectMapper.convertValue(node, toValueTypeRef);
		} catch (Exception e) {
			LOG.error("JackJsonUtil.convertValue error: ", e);
		}
		return null;
	}

	public static <T> T convertValue(JsonNode node, Class<T> toValueType) {
		try {
			return objectMapper.convertValue(node, toValueType);
		} catch (Exception e) {
			LOG.error("JackJsonUtil.convertValue error: ", e);
		}
		return null;
	}

	public static String toJson(Object obj) {
		if (Objects.isNull(obj)) {
			return null;
		} else if (obj instanceof String) {
			return (String) obj;
		}
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			LOG.error("JackJsonUtil.toJsonString error: ", e);
		}
		return null;
	}

	public static <T> T toBean(String str, Class<T> cls) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		try {
			return objectMapper.readValue(str, cls);
		} catch (Exception e) {
			LOG.error("JackJsonUtil.toObject error: ", e);
		}
		return null;
	}

	public static <T> T toBean(String str, TypeReference<T> typeReference) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		try {
			return objectMapper.readValue(str, typeReference);
		} catch (Exception e) {
			LOG.error("JackJsonUtil.toObject error: ", e);
		}
		return null;
	}
}
