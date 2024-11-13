package br.com.fiap.gs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info =
@Info(title = "API GS", version = "0.0.1"))
public class OdontoprevApplication {

	public static void main(String[] args) {
		SpringApplication.run(OdontoprevApplication.class, args);
	}

}
