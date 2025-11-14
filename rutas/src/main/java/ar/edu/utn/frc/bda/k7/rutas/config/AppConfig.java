package ar.edu.utn.frc.bda.k7.rutas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    @Value("${ms.maps.url}")
    private String geoApiUrl;

    @Bean
    public RestClient geoApiRestClient() {
        return RestClient.builder()
                .baseUrl(geoApiUrl).build();
    }
}
