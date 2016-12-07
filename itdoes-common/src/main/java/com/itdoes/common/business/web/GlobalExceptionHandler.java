package com.itdoes.common.business.web;

import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import com.itdoes.common.core.Result;
import com.itdoes.common.core.util.Validators;
import com.itdoes.common.core.web.MediaTypes;

/**
 * @author Jalen Zhong
 * @See org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	private static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e,
			WebRequest request) {
		final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		final Map<String, String> errors = Validators.propertyMessages(e);
		final Result result = Result.fail(httpStatus.value(), errors);
		return handleExceptionInternal(e, result, null, httpStatus, request);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public final ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
		final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		return handleExceptionInternal(e, null, null, httpStatus, request);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<?> handleException(Exception e, WebRequest request) {
		final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		return handleExceptionInternal(e, null, null, httpStatus, request);
	}

	protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		LOGGER.error("Error occurred", e);

		if (body == null) {
			body = Result.fail(status.value(), e.getMessage());
		}

		if (headers == null) {
			headers = new HttpHeaders();
		}
		headers.setContentType(MediaType.parseMediaType(MediaTypes.APPLICATION_JSON_UTF_8));

		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, e, WebRequest.SCOPE_REQUEST);
		}
		return new ResponseEntity<Object>(body, headers, status);
	}
}
