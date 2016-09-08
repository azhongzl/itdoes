package com.itdoes.common.core.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author Jalen Zhong
 */
public class JavaTimeObjectMapper extends ObjectMapper {
	private static final long serialVersionUID = -2765335605123454485L;

	public JavaTimeObjectMapper() {
		this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		this.registerModule(new JavaTimeModule());
	}
}
