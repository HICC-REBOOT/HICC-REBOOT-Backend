package hiccreboot.backend.common.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
	private final boolean requireCredential;
	private final List<String> allowedOrigins;
	private final List<String> allowedMethods;
	private final List<String> allowedHeaders;
}