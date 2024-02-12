package hiccreboot.backend.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {
	private final String host;
	private final int port;
	private final String username;
	private final String password;
}
