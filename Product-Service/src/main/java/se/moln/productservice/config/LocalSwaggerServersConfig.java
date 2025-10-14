package se.moln.productservice.config;

import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile({"dev","local"})
public class LocalSwaggerServersConfig {

    @Bean
    public OpenApiCustomizer localServers(@Value("${server.port:8081}") int port) {
        return openApi -> openApi.setServers(
                List.of(new Server()
                        .url("http://localhost:" + port)
                        .description("Local development")));
    }
}
