package hiccreboot.backend.common.auth.login.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class StudentNumberPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final String DEFAULT_LOGIN_URL = "/api/auth/login";
	private static final String DEFAULT_LOGIN_HTTP_METHOD = "POST";
	private static final String CONTENT_TYPE = "application/json";
	private static final AntPathRequestMatcher REQUEST_MATCHER = new AntPathRequestMatcher(DEFAULT_LOGIN_URL,
		DEFAULT_LOGIN_HTTP_METHOD);

	private final ObjectMapper objectMapper;

	private static final String SECURITY_FORM_USERNAME_KEY = "studentNumber";
	private static final String SECURITY_FORM_PASSWORD_KEY = "password";

	public StudentNumberPasswordAuthenticationFilter(ObjectMapper objectMapper) {
		super(REQUEST_MATCHER);
		this.objectMapper = objectMapper;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException,
		IOException,
		ServletException {

		if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
			throw new AuthenticationServiceException(
				"Authentication Content-Type not supported: " + request.getContentType());
		}

		String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

		Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

		String username = usernamePasswordMap.get(SECURITY_FORM_USERNAME_KEY);
		String password = usernamePasswordMap.get(SECURITY_FORM_PASSWORD_KEY);

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
			username, password);

		return this.getAuthenticationManager().authenticate(authRequest);
	}
}
