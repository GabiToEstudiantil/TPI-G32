package ar.edu.frc.utn.bda.k7.solicitudes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    @Value("${ms.rutas.url}")
    private String rutasApiUrl;

    @Bean
    public RestClient rutasApiClient() {
        return RestClient.builder()
                .baseUrl(rutasApiUrl)
                .build();
    }
}
