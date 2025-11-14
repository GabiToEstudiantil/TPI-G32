package ar.edu.utn.frc.bda.k7.geoapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    @Bean
    public RestClient googleMapsRestClient() {
        return RestClient.builder()
                .baseUrl("https://maps.googleapis.com/maps/api")
                .build();
    }
}
