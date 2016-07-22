package com.itdoes.common.jackson;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

/**
 * @author Jalen Zhong
 */
public class JsonMapper {
	private static final JsonMapper NON_EMPTY_MAPPER = new JsonMapper(Include.NON_EMPTY);
	private static final JsonMapper NON_DEFAULT_MAPPER = new JsonMapper(Include.NON_DEFAULT);

	public static JsonMapper nonEmptyMapper(boolean createNew) {
		return createNew ? new JsonMapper(Include.NON_EMPTY) : NON_EMPTY_MAPPER;
	}

	public static JsonMapper nonDefaultMapper(boolean createNew) {
		return createNew ? new JsonMapper(Include.NON_DEFAULT) : NON_DEFAULT_MAPPER;
	}

	private final ObjectMapper mapper;

	public JsonMapper() {
		this(null);
	}

	public JsonMapper(Include include) {
		mapper = new ObjectMapper();
		if (include != null) {
			mapper.setSerializationInclusion(include);
		}
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	public String toJson(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("WriteValueAsString failed for Object: " + object, e);
		}
	}

	public <T> T fromJson(String jsonString, Class<T> clazz) {
		try {
			return mapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			throw new IllegalArgumentException("ReadValue failed for Class: " + clazz + ", json: " + jsonString, e);
		}
	}

	public <T> T fromJson(String jsonString, JavaType javaType) {
		try {
			return mapper.readValue(jsonString, javaType);
		} catch (IOException e) {
			throw new IllegalArgumentException("ReadValue failed for JavaType: " + javaType + ", json: " + jsonString,
					e);
		}
	}

	public JavaType buildCollectionType(Class<? extends Collection<?>> collectionClass, Class<?> elementClass) {
		return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
	}

	public JavaType buildMapType(Class<? extends Map<?, ?>> mapClass, Class<?> keyClass, Class<?> valueClass) {
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

	public void enableEnumUsingToString() {
		mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
	}

	public void enableJaxb() {
		final JaxbAnnotationModule module = new JaxbAnnotationModule();
		mapper.registerModule(module);
	}

	public ObjectMapper getMapper() {
		return mapper;
	}
}
