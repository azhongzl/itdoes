package com.itdoes.common.business.web;

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

import com.itdoes.common.core.Result;
import com.itdoes.common.core.util.Validators;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<?> handleException(ConstraintViolationException e, WebRequest request) {
		final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		final Map<String, String> errors = Validators.propertyMessages(e);
		final Result result = HttpResults.fail(httpStatus.value(), errors);
		return handleExceptionInternal(e, result, newJsonHttpHeaders(), httpStatus, request);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<?> handleException(RuntimeException e, WebRequest request) {
		final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		final Result result = HttpResults.fail(httpStatus.value(), e.getMessage());
		return handleExceptionInternal(e, result, newJsonHttpHeaders(), httpStatus, request);
	}

	private static HttpHeaders newJsonHttpHeaders() {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MediaTypes.APPLICATION_JSON_UTF_8));
		return headers;
	}
}
