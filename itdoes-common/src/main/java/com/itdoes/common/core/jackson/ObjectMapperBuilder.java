package com.itdoes.common.core.jackson;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.google.common.collect.Sets;

/**
 * @author Jalen Zhong
 */
public class ObjectMapperBuilder {
	public static ObjectMapperBuilder newBuilder() {
		return new ObjectMapperBuilder();
	}

	private Include serializationInclusion;
	private final Set<SerializationFeature> enableSerializationFeatures = Sets.newHashSet();
	private final Set<DeserializationFeature> enableDeserializationFeatures = Sets.newHashSet();
	private final Set<SerializationFeature> disableSerializationFeatures = Sets.newHashSet();
	private final Set<DeserializationFeature> disableDeserializationFeatures = Sets.newHashSet();
	private final Set<Module> registerModules = Sets.newHashSet();

	public ObjectMapperBuilder setSerializationInclusion(Include include) {
		serializationInclusion = include;
		return this;
	}

	public ObjectMapperBuilder enable(SerializationFeature feature) {
		enableSerializationFeatures.add(feature);
		return this;
	}

	public ObjectMapperBuilder enable(DeserializationFeature feature) {
		enableDeserializationFeatures.add(feature);
		return this;
	}

	public ObjectMapperBuilder disable(SerializationFeature feature) {
		disableSerializationFeatures.add(feature);
		return this;
	}

	public ObjectMapperBuilder disable(DeserializationFeature feature) {
		disableDeserializationFeatures.add(feature);
		return this;
	}

	public ObjectMapperBuilder registerModule(Module module) {
		registerModules.add(module);
		return this;
	}

	public ObjectMapperBuilder nonEmpty() {
		return setSerializationInclusion(Include.NON_EMPTY);
	}

	public ObjectMapperBuilder nonDefault() {
		return setSerializationInclusion(Include.NON_DEFAULT);
	}

	public ObjectMapperBuilder enableEnumsUsingToString() {
		return enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
				.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
	}

	public ObjectMapperBuilder disableFailOnUnknownProperties() {
		return disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	public ObjectMapperBuilder registerModuleJaxb() {
		return registerModule(new JaxbAnnotationModule());
	}

	public ObjectMapper build() {
		final ObjectMapper mapper = new ObjectMapper();

		if (serializationInclusion != null) {
			mapper.setSerializationInclusion(serializationInclusion);
		}
		for (SerializationFeature feature : enableSerializationFeatures) {
			mapper.enable(feature);
		}
		for (DeserializationFeature feature : enableDeserializationFeatures) {
			mapper.enable(feature);
		}
		for (SerializationFeature feature : disableSerializationFeatures) {
			mapper.disable(feature);
		}
		for (DeserializationFeature feature : disableDeserializationFeatures) {
			mapper.disable(feature);
		}
		for (Module module : registerModules) {
			mapper.registerModule(module);
		}

		return mapper;
	}

	private ObjectMapperBuilder() {
	}
}
