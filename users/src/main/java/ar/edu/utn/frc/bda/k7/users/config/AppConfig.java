package ar.edu.utn.frc.bda.k7.users.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${ms.keycloak.url}")
    private String keycloakApiUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
