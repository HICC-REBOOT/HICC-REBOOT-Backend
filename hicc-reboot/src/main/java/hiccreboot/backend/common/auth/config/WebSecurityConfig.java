package hiccreboot.backend.common.auth.config;

import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import hiccreboot.backend.common.auth.jwt.TokenProvider;
import hiccreboot.backend.common.auth.jwt.filter.JwtAuthenticationFilter;
import hiccreboot.backend.common.auth.login.filter.StudentNumberPasswordAuthenticationFilter;
import hiccreboot.backend.common.auth.login.handler.LoginFailureHandler;
import hiccreboot.backend.common.auth.login.handler.LoginSuccessHandler;
import hiccreboot.backend.common.auth.login.service.LoginService;
import hiccreboot.backend.repository.member.MemberRepository;
import hiccreboot.backend.repository.refreshToken.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final TokenProvider tokenProvider;
	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final ObjectMapper objectMapper;
	private final LoginService loginService;

	private final LoginSuccessHandler loginSuccessHandler;
	private final LoginFailureHandler loginFailureHandler;

	private static String[] ALLOWED_PATTERN = new String[] {
		"/api/login/**",
		"/api/sign-up**",
		"/api/logout**",
	};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
				.requestMatchers(Stream.of(ALLOWED_PATTERN)
					.map(AntPathRequestMatcher::antMatcher)
					.toArray(AntPathRequestMatcher[]::new))
				.permitAll()
				.anyRequest()
				.authenticated()) // for develop
			.logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
				.logoutUrl("/api/logout")
				.logoutSuccessHandler((request, response, authentication) -> {
					tokenProvider.extractStudentNumber(request)
						.ifPresent(tokenProvider::disableRefreshToken);
				})
				.deleteCookies())
			.addFilterAfter(jwtAuthenticationFilter(), LogoutFilter.class)
			.addFilterAfter(studentNumberPasswordAuthenticationFilter(), JwtAuthenticationFilter.class)
			.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(tokenProvider, memberRepository, refreshTokenRepository);
	}

	@Bean
	public StudentNumberPasswordAuthenticationFilter studentNumberPasswordAuthenticationFilter() {
		StudentNumberPasswordAuthenticationFilter authenticationFilter = new StudentNumberPasswordAuthenticationFilter(
			objectMapper);

		authenticationFilter.setAuthenticationManager(authenticationManager());
		authenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
		authenticationFilter.setAuthenticationFailureHandler(loginFailureHandler);

		return authenticationFilter;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		provider.setUserDetailsService(loginService);
		provider.setPasswordEncoder(passwordEncoder());

		return new ProviderManager(provider);
	}

}
