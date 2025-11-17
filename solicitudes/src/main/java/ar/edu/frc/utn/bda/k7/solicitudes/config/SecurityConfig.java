package ar.edu.frc.utn.bda.k7.solicitudes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    // Health checks públicos
                    .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                    
                    // Solicitudes
                    .requestMatchers("GET", "/api/solicitudes").hasRole("ADMIN")
                    .requestMatchers("GET", "/api/solicitudes/cliente/{dni}").hasAnyRole("ADMIN", "CLIENTE")
                    .requestMatchers("GET", "/api/solicitudes/{solicitudId}").hasAnyRole("ADMIN", "CLIENTE")
                    .requestMatchers("DELETE", "/api/solicitudes/{solicitudId}").hasAnyRole("ADMIN", "CLIENTE")
                    .requestMatchers("POST", "/api/solicitudes").hasAnyRole("ADMIN", "CLIENTE")
                    .requestMatchers("GET", "/api/solicitudes/{solicitudId}/tracking").hasAnyRole("ADMIN", "CLIENTE")
                    
                    // Tramos - Asignar/Desasignar camión
                    .requestMatchers("PATCH", "/api/solicitudes/{solicitudId}/tramos/{tramoId}/asignar-camion").hasRole("ADMIN")
                    .requestMatchers("PATCH", "/api/solicitudes/{solicitudId}/tramos/{tramoId}/desasignar-camion").hasRole("ADMIN")
                    
                    // Tramos - Actualizar estado
                    .requestMatchers("PATCH", "/api/solicitudes/{solicitudId}/tramos/{tramoId}/actualizar-estado").hasAnyRole("ADMIN", "TRANSPORTISTA")
                    
                    // Contenedores
                    .requestMatchers("GET", "/api/contenedores/cliente/{dni}").hasAnyRole("ADMIN", "CLIENTE")
                    .requestMatchers("POST", "/api/contenedores").hasAnyRole("ADMIN", "CLIENTE")
                    .requestMatchers("GET", "/api/contenedores/{contenedorId}").hasAnyRole("ADMIN", "CLIENTE")
                    .requestMatchers("PUT", "/api/contenedores/{contenedorId}").hasAnyRole("ADMIN", "CLIENTE")
                    
                    // Todo lo demás requiere autenticación
                    .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }

    static class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            // Leer roles de realm_access
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            
            if (realmAccess != null && realmAccess.containsKey("roles")) {
                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) realmAccess.get("roles");
                
                return roles.stream()
                    .filter(role -> role.startsWith("ROLE_")) // Solo los que empiezan con ROLE_
                    .map(SimpleGrantedAuthority::new) // Ya tienen el prefijo, no agregarlo de nuevo
                    .collect(Collectors.toList());
            }
            
            return Collections.emptyList();
        }
    }
}