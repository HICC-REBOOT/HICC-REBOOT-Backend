package hiccreboot.backend.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import hiccreboot.backend.common.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

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
		BaseErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_EXCEPTION;

		ErrorResponse errorResponse = ErrorResponse.fromErrorCode(errorCode, request.getRequestURI());

		return ResponseEntity.status(errorResponse.getStatus())
			.body(errorResponse);
	}
}
