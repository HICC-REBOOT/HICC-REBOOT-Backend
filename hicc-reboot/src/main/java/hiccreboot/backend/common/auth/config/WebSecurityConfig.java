package hiccreboot.backend.common.auth.config;

import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
	private static AntPathRequestMatcher[] PERMIT_ALL_PATTERNS() {
		String[] PATTERNS = new String[] {
			"api/login/**",
			"api/sing-up/**",
			"api/logout/**"
		};
		return Stream.of(PATTERNS)
			.map(AntPathRequestMatcher::antMatcher)
			.toArray(AntPathRequestMatcher[]::new);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
				.requestMatchers(PERMIT_ALL_PATTERNS())
				.permitAll()
				.anyRequest()
				.permitAll()) // for develop
			// .authenticated())
			.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
