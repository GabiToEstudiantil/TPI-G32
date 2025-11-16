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
        @Value("${service.url.rutas}") String rutasUrl,
        @Value("${service.url.users}") String usersUrl,
        @Value("${service.url.solicitudes}") String solicitudesUrl,
        @Value("${service.url.geoapi}") String geoapiUrl,
        @Value("${service.url.keycloak}") String keycloakUrl,
        @Value("${service.path.rutas}") String rutasPath,
        @Value("${service.path.users}") String usersPath,
        @Value("${service.path.solicitudes}") String solicitudesPath,
        @Value("${service.path.contenedores}") String contenedoresPath,
        @Value("${service.path.geoapi}") String geoapiPath,
        @Value("${service.path.keycloak}") String keycloakPath
        ) {
        
        return builder.routes()
        .route("rutas", r -> r.path(rutasPath)
                .filters(f -> f.removeRequestHeader("Cookie"))
                .uri(rutasUrl))
        
        .route("users", r -> r.path(usersPath)
                .filters(f -> f.removeRequestHeader("Cookie"))
                .uri(usersUrl))
        
        .route("solicitudes", r -> r.path(solicitudesPath)
                .filters(f -> f.removeRequestHeader("Cookie"))
                .uri(solicitudesUrl))
        
        .route("contenedores", r -> r.path(contenedoresPath)
                .filters(f -> f.removeRequestHeader("Cookie"))
                .uri(solicitudesUrl))
        
        .route("geoapi", r -> r.path(geoapiPath)
                .filters(f -> f.removeRequestHeader("Cookie"))
                .uri(geoapiUrl))
        
        .route("keycloak", r -> r.path(keycloakPath)
                .filters(f -> f.removeRequestHeader("Cookie"))
                .uri(keycloakUrl))
        
        .build();
}
}
