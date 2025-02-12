package hiccreboot.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(servers = {@Server(url = "/", description = "https://www.hicc.co.kr")})
public class HiccRebootApplication {

	public static void main(String[] args) {
		SpringApplication.run(HiccRebootApplication.class, args);
	}

}
