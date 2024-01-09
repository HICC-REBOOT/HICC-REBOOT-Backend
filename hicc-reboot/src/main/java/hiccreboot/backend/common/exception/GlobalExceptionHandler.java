package hiccreboot.backend.common.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import hiccreboot.backend.common.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ErrorResponse customExceptionHandler(CustomException e, HttpServletRequest request) {
		return ErrorResponse.fromCustomException(e, request.getRequestURI());
	}
}
