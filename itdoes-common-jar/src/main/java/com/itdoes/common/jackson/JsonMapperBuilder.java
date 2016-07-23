package com.itdoes.common.jackson;

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
public class JsonMapperBuilder {
	public static JsonMapperBuilder defaultBuilder() {
		return new JsonMapperBuilder().disableFailOnUnknownProperties();
	}

	public static JsonMapperBuilder nonEmptyBuilder() {
		return defaultBuilder().setSerializationInclusionNonEmpty();
	}

	public static JsonMapperBuilder nonDefaultBuilder() {
		return defaultBuilder().setSerializationInclusionNonDefault();
	}

	private final Set<Include> serializationInclusions = Sets.newHashSet();
	private final Set<SerializationFeature> enableSerializationFeatures = Sets.newHashSet();
	private final Set<DeserializationFeature> enableDeserializationFeatures = Sets.newHashSet();
	private final Set<SerializationFeature> disableSerializationFeatures = Sets.newHashSet();
	private final Set<DeserializationFeature> disableDeserializationFeatures = Sets.newHashSet();
	private final Set<Module> registerModules = Sets.newHashSet();

	public JsonMapperBuilder setSerializationInclusion(Include include) {
		serializationInclusions.add(include);
		return this;
	}

	public JsonMapperBuilder enable(SerializationFeature feature) {
		enableSerializationFeatures.add(feature);
		return this;
	}

	public JsonMapperBuilder enable(DeserializationFeature feature) {
		enableDeserializationFeatures.add(feature);
		return this;
	}

	public JsonMapperBuilder disable(SerializationFeature feature) {
		disableSerializationFeatures.add(feature);
		return this;
	}

	public JsonMapperBuilder disable(DeserializationFeature feature) {
		disableDeserializationFeatures.add(feature);
		return this;
	}

	public JsonMapperBuilder registerModule(Module module) {
		registerModules.add(module);
		return this;
	}

	public JsonMapperBuilder setSerializationInclusionNonEmpty() {
		return setSerializationInclusion(Include.NON_EMPTY);
	}

	public JsonMapperBuilder setSerializationInclusionNonDefault() {
		return setSerializationInclusion(Include.NON_DEFAULT);
	}

	public JsonMapperBuilder enableEnumsUsingToString() {
		return enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
				.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
	}

	public JsonMapperBuilder disableFailOnUnknownProperties() {
		return disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	public JsonMapperBuilder registerModuleJaxb() {
		return registerModule(new JaxbAnnotationModule());
	}

	public JsonMapper build() {
		final ObjectMapper mapper = new ObjectMapper();

		for (Include include : serializationInclusions) {
			mapper.setSerializationInclusion(include);
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

		return new JsonMapper(mapper);
	}

	private JsonMapperBuilder() {
	}
}
