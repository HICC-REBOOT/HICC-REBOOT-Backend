package hiccreboot.backend.common.auth.login.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import hiccreboot.backend.common.dto.DataResponse;
import hiccreboot.backend.common.util.ResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) {
		response.setStatus(HttpServletResponse.SC_OK);//보안을 위해 로그인 오류지만 200 반환

		ResponseWriter.writeResponse(response, DataResponse.ok());
	}
}
