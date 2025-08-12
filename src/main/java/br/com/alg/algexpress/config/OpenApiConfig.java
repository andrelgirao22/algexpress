package br.com.alg.algexpress.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local Development Server")
                ))
                .info(new Info()
                        .title("AlgExpress API")
                        .description("Sistema completo de gerenciamento de pedidos e entregas para pizzarias")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("ALG Development Team")
                                .email("dev@algexpress.com"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://algexpress.com/license"))
                );
    }
}