package hiccreboot.backend.common.auth.config;

import java.util.List;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

	private static final String PRESIDENT = "PRESIDENT";
	private static final String[] PRESIDENT_AND_EXECUTIVE = new String[] {"PRESIDENT", "EXECUTIVE"};

	private static final String[] ALLOWED_PATTERN = new String[] {
		"/api/auth/login/**",
		"/api/auth/sign-up/**",
		"/api/auth/duplicate/**",
		"/api/auth/departments",
		"/api/auth/logout/**",
		"/api/main/**",
		"/swagger-ui/**",
		"/api-docs/**"
	};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
				.requestMatchers(Stream.of(ALLOWED_PATTERN)
					.map(AntPathRequestMatcher::antMatcher)
					.toArray(AntPathRequestMatcher[]::new))
				.permitAll()
				.requestMatchers(AntPathRequestMatcher.antMatcher("/api/admin/president/**")).hasRole(PRESIDENT)
				.requestMatchers(AntPathRequestMatcher.antMatcher("/api/admin/**")).hasAnyRole(PRESIDENT_AND_EXECUTIVE)
				.anyRequest()
				.authenticated()) // for develop
			.logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
				.logoutUrl("/api/auth/logout")
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

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowCredentials(true);
		configuration.setAllowedOriginPatterns(List.of("http://localhost:3000"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "PUT"));
		configuration.setAllowedHeaders(List.of("*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

}
