package org.nenad.paunov;

import org.nenad.paunov.config.web.RestTemplateErrorHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PlayerServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PlayerServiceApplication.class, args);
	}

	@Bean
	public RestTemplateErrorHandler restTemplateErrorHandler() {
		RestTemplateErrorHandler errorHandler = new RestTemplateErrorHandler();
		errorHandler.setServiceName("Game service");
		return errorHandler;
	}
}