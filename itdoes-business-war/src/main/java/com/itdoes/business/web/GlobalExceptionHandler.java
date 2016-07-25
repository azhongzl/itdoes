package com.itdoes.business.web;

import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.itdoes.common.jackson.JsonMapper;
import com.itdoes.common.jackson.JsonMapperBuilder;
import com.itdoes.common.util.Validators;
import com.itdoes.common.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	private static final JsonMapper JSON_MAPPER = JsonMapperBuilder.newBuilder().build();

	@ExceptionHandler(value = { ConstraintViolationException.class })
	public final ResponseEntity<?> handleException(ConstraintViolationException e, WebRequest request) {
		final Map<String, String> errors = Validators.propertyMessages(e);
		final String body = JSON_MAPPER.toJson(errors);
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MediaTypes.TEXT_PLAIN_UTF_8));
		return handleExceptionInternal(e, body, headers, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = { Exception.class })
	public final ResponseEntity<?> handleException(RuntimeException e, WebRequest request) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MediaTypes.TEXT_PLAIN_UTF_8));
		return handleExceptionInternal(e, e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}
