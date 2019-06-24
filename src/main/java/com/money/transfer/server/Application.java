package com.money.transfer.server;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
	    info = @Info(
	        title = "Sample Application",
	        version = "1.0",
	        description = "Sample API",
	        contact = @Contact(url = "https://piotrminkowski.wordpress.com", name = "Piotr Mińkowski", email = "piotr.minkowski@gmail.com")
	    )
	)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}