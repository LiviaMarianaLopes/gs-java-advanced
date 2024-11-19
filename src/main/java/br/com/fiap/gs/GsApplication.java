package br.com.fiap.gs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "br.com.fiap.gs.repository")
@OpenAPIDefinition(info =
@Info(title = "API GS", version = "0.0.1"))
public class GsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GsApplication.class, args);
	}

}
