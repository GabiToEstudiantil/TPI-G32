package ar.edu.utn.frc.bda.k7.api_gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GWConfig {
    
    @Bean
    public RouteLocator configPaths(RouteLocatorBuilder builder,
        @Value ("localhost:8181") String puertoRuta,
        @Value ("/api/ruta/camiones") String uriCamiones,
        @Value ("/api/ruta/depositos") String uriDepositos,
        @Value ("/api/ruta/ubicaciones") String uriUbicaciones,
        @Value ("/api/ruta/tarifas/combustible") String uriTaCombustible,
        @Value ("/api/ruta/tarifas/volumen") String uriTaVolumen,
        @Value ("/api/ruta/**") String uriRuta
        ) {
        return builder.routes()
        .route(r -> r.path(uriCamiones).filters(f -> f.rewritePath(uriCamiones, "/camiones")).uri(puertoRuta + uriCamiones))
        .build();
    }

    @Bean
    public RouteLocator configurarRutasUsuarios (RouteLocatorBuilder builder){
        return builder.routes()
            .route(r -> r
            .path("/api/users/**")
            .uri("http://localhost:8080"))
            .build();
    }
}
