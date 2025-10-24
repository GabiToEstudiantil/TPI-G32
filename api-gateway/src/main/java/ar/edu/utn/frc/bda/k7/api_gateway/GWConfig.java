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
        @Value ("http://localhost:") String baseHost, //Esta declaracion de cosas me dijo copylot de meterlas en el .properties
        @Value ("/api/ruta/**") String uriRuta,       //Pero nose, dsp lo veremos :]
        @Value ("/api/users/**") String uriUsuarios,
        @Value ("/api/solicitudes/**") String uriSolicitudes,
        @Value ("/api/maps/**") String uriMaps,
        @Value ("/api/auth/**") String uriAuth
        ) {
        return builder.routes()
        .route(r -> r.path(uriRuta).uri(baseHost + "8181"))
        .route(r -> r.path(uriUsuarios).uri(baseHost + "8080"))
        .route(r -> r.path(uriSolicitudes).uri(baseHost + "8081"))
        .route(r -> r.path(uriMaps).uri(baseHost + "9090"))
        .route(r -> r.path(uriAuth).uri(baseHost + "9091"))
        .build();
    }
}
