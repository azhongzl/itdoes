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

import com.itdoes.common.business.BaseController;
import com.itdoes.common.business.Result;
import com.itdoes.common.util.Validators;
import com.itdoes.common.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(value = { ConstraintViolationException.class })
	public final ResponseEntity<?> handleException(ConstraintViolationException e, WebRequest request) {
		final Map<String, String> errors = Validators.propertyMessages(e);
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MediaTypes.APPLICATION_JSON_UTF_8));
		final Result result = new Result(HttpStatus.BAD_REQUEST.value(), errors, null);
		final String jsonResult = BaseController.toJson(result);
		return handleExceptionInternal(e, jsonResult, headers, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = { Exception.class })
	public final ResponseEntity<?> handleException(RuntimeException e, WebRequest request) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MediaTypes.APPLICATION_JSON_UTF_8));
		final Result result = new Result(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
		final String jsonResult = BaseController.toJson(result);
		return handleExceptionInternal(e, jsonResult, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}
