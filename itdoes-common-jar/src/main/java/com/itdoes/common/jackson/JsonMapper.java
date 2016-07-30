package com.itdoes.common.jackson;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

/**
 * @author Jalen Zhong
 */
public class JsonMapper {
	private final ObjectMapper mapper;

	protected JsonMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public String toJson(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("WriteValueAsString failed for Object: " + object, e);
		}
	}

	public <T> T fromJson(String jsonString, Class<T> clazz) {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}

		try {
			return mapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			throw new IllegalArgumentException("ReadValue failed for Class: " + clazz + ", json: " + jsonString, e);
		}
	}

	public <T> T fromJson(String jsonString, JavaType javaType) {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}

		try {
			return mapper.readValue(jsonString, javaType);
		} catch (IOException e) {
			throw new IllegalArgumentException("ReadValue failed for JavaType: " + javaType + ", json: " + jsonString,
					e);
		}
	}

	@SuppressWarnings("rawtypes")
	public JavaType buildCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
		return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
	}

	@SuppressWarnings("rawtypes")
	public JavaType buildMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
		return mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
	}

	public void update(String jsonString, Object object) {
		try {
			mapper.readerForUpdating(object).readValue(jsonString);
		} catch (IOException e) {
			throw new IllegalArgumentException("ReadValue failed for Object: " + object + ", json: " + jsonString, e);
		}
	}

	public String toJsonP(String functionName, Object object) {
		return toJson(new JSONPObject(functionName, object));
	}

	public ObjectMapper getMapper() {
		return mapper;
	}
}
