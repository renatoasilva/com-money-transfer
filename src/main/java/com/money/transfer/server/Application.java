package com.money.transfer.server;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
		info = @Info(
				title = "Money Transfer Application",
				version = "1.0",
				description = "Money Transfer API",
				contact = @Contact(url = "https://github.com/renatoasilva/com-money-transfer", name = "Renato Silva", email = "eng.renato.silva@gmail.com")
				)
	)
public class Application {

	public static void main(String[] args) {
		Micronaut.run(Application.class);
	}
}