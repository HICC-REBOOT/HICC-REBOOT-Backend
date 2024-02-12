package hiccreboot.backend.common.auth.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import hiccreboot.backend.common.exception.GlobalErrorCode;
import hiccreboot.backend.common.exception.dto.ErrorResponse;
import hiccreboot.backend.common.util.ResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) {

		ErrorResponse errorResponse = ErrorResponse.fromErrorCode(GlobalErrorCode.ACCESS_DENIED,
			request.getRequestURI());

		ResponseWriter.writeResponse(response, errorResponse);
	}

}
