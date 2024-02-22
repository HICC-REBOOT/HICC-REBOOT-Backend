package hiccreboot.backend.common.exception;

import static hiccreboot.backend.common.exception.GlobalErrorCode.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import hiccreboot.backend.common.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	public ResponseEntity<Object> handleExceptionInternal(
		Exception ex,
		@Nullable Object body,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request) {
		ServletWebRequest servletWebRequest = (ServletWebRequest)request;
		String path = servletWebRequest.getRequest().getRequestURI();

		ErrorResponse errorResponse = ErrorResponse.create(status, ex.getMessage(), path);

		return super.handleExceptionInternal(ex, errorResponse, headers, status, request);
	}

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request) {
		ServletWebRequest servletWebRequest = (ServletWebRequest)request;
		String path = servletWebRequest.getRequest().getRequestURI();

		ErrorResponse errorResponse = ErrorResponse.fromErrorCode(MESSAGE_NOT_READABLE, path);

		return ResponseEntity.status(errorResponse.getStatus())
			.body(errorResponse);
	}

	@Override
	public ResponseEntity<Object> handleHttpMessageNotReadable(
		HttpMessageNotReadableException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request) {
		ServletWebRequest servletWebRequest = (ServletWebRequest)request;
		String path = servletWebRequest.getRequest().getRequestURI();

		ErrorResponse errorResponse = ErrorResponse.fromErrorCode(MESSAGE_NOT_READABLE, path);

		return ResponseEntity.status(errorResponse.getStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> customExceptionHandler(CustomException e, HttpServletRequest request) {
		log.error("{} Custom Exception: {}", e.getErrorCode(), request.getRequestURI());

		ErrorResponse errorResponse = ErrorResponse.fromCustomException(e, request.getRequestURI());

		return ResponseEntity.status(errorResponse.getStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
		log.error("INTERNAL_SERVER_ERROR: {} {}", e, request.getRequestURI());

		ErrorResponse errorResponse = ErrorResponse.fromErrorCode(INTERNAL_SERVER_EXCEPTION, request.getRequestURI());

		return ResponseEntity.status(errorResponse.getStatus())
			.body(errorResponse);
	}
}
