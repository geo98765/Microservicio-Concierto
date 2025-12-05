package com.example.concert_service.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

        @Value("${server.port:8082}")
        private String serverPort;

        @Bean
        public OpenAPI customOpenAPI() {
                // Detectar si estamos en Render o en local
                String renderUrl = System.getenv("RENDER_EXTERNAL_URL");

                List<Server> servers;
                if (renderUrl != null && !renderUrl.isEmpty()) {
                        // En Render - usar URL de producción primero
                        servers = List.of(
                                        new Server()
                                                        .url(renderUrl)
                                                        .description("Concert Service - Render Production"),
                                        new Server()
                                                        .url("http://localhost:" + serverPort)
                                                        .description("Concert Service - Local Development"));
                } else {
                        // En local - solo usar localhost
                        servers = List.of(
                                        new Server()
                                                        .url("http://localhost:" + serverPort)
                                                        .description("Concert Service - Local Development"));
                }

                return new OpenAPI()
                                .info(new Info()
                                                .title("Concert Service API")
                                                .version("1.0.0")
                                                .description("""
                                                                API for concert discovery, artists and nearby venue services.
                                                                Part of RockStadium Microservices.
                                                                """)
                                                .contact(new Contact()
                                                                .name("RockStadium Team")
                                                                .email("support@rockstadium.com"))
                                                .license(new License()
                                                                .name("Apache 2.0")
                                                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                                .servers(servers);
        }
}
