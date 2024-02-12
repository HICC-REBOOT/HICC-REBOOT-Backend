package hiccreboot.backend.common.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import hiccreboot.backend.common.properties.CorsProperties;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CorsConfig {
	private final CorsProperties corsProperties;
	private static final String ALL_PATTERN = "/**";

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowCredentials(corsProperties.isRequireCredential());
		configuration.setAllowedMethods(corsProperties.getAllowedMethods());
		configuration.setAllowedOrigins(corsProperties.getAllowedOrigins());
		configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(ALL_PATTERN, configuration);

		return source;
	}
}
