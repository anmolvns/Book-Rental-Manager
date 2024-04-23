package com.library.bookrental.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Book Rental Manager",
                version = "1.0",
                contact = @Contact(
                        name = "Anmol's Team",
                        url = "github.com/anmolvns",
                        email = "bookrental@gmail.com"
                ),
                description = "API for Book Rental Manager ",
                termsOfService = "Terms of service: By using the Book Rental API, you agree to comply with the terms and conditions outlined in our official documentation."
        ),
        servers = {
                @Server(
                        description = "LOCAL ENV",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "http://book-rental.library.com"
                )
        }
)
public class SwaggerConfig {

}
