package hiccreboot.backend.common.conf;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {
	@Value("${email.host}")
	private String host;

	@Value("${email.port}")
	private int port;

	@Value("${email.username}")
	private String username;

	@Value("${email.password}")
	private String password;

	@Value("${email.properties.smtp.auth}")
	private boolean auth;

	@Value("${email.properties.smtp.starttls.enabled}")
	private boolean startTlsEnabled;

	@Value("${email.properties.smtp.starttls.required}")
	private boolean startTlsRequired;

	@Value("${email.properties.smtp.connectiontimeout}")
	private int connectionTimeout;

	@Value("${email.properties.smtp.timeout}")
	private int timeout;

	@Value("${email.properties.smtp.writetimeout}")
	private int writeTimeout;

	private static final String DEFAULT_ENCODING = "UTF-8";

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		mailSender.setDefaultEncoding(DEFAULT_ENCODING);

		mailSender.setJavaMailProperties(mailProperties());

		return mailSender;
	}

	private Properties mailProperties() {
		Properties properties = new Properties();

		properties.put("mail.smtp.auth", auth);
		properties.put("mail.smtp.starttls.enable", startTlsEnabled);
		properties.put("mail.smtp.starttls.required", startTlsRequired);
		properties.put("mail.smtp.connectiontimeout", connectionTimeout);
		properties.put("mail.smtp.timeout", timeout);
		properties.put("mail.smtp.writetimeout", writeTimeout);

		return properties;
	}
}
