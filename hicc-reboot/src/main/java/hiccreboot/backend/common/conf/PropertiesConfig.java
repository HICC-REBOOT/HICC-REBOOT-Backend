package hiccreboot.backend.common.conf;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import hiccreboot.backend.common.properties.CorsProperties;
import hiccreboot.backend.common.properties.EmailProperties;
import hiccreboot.backend.common.properties.JwtProperties;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({
	CorsProperties.class,
	JwtProperties.class,
	EmailProperties.class})
public class PropertiesConfig {

}
