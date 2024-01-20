package hiccreboot.backend.common.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import hiccreboot.backend.common.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ErrorResponse customExceptionHandler(CustomException e, HttpServletRequest request) {
		return ErrorResponse.fromCustomException(e, request.getRequestURI());
	}

	@ExceptionHandler(Exception.class)
	public ErrorResponse handleException(Exception e, HttpServletRequest request) {
		log.error("INTERNAL_SERVER_ERROR: ", e);
		BaseErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_EXCEPTION;
		String path = request.getRequestURI();

		return ErrorResponse.fromErrorCode(errorCode, path);
	}
}
