package ar.edu.utn.frc.bda.k7.rutas.config;

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
                    
                    // Camiones
                    .requestMatchers("GET", "/api/ruta/camiones").hasRole("ADMIN")
                    .requestMatchers("POST", "/api/ruta/camiones").hasAnyRole("ADMIN", "TRANSPORTISTA")
                    .requestMatchers("GET", "/api/ruta/camiones/{dominio}").hasAnyRole("ADMIN", "TRANSPORTISTA")
                    
                    // Depósitos
                    .requestMatchers("GET", "/api/ruta/depositos").hasRole("ADMIN")
                    .requestMatchers("POST", "/api/ruta/depositos").hasRole("ADMIN")
                    .requestMatchers("PUT", "/api/ruta/depositos/{depositoId}").hasRole("ADMIN")
                    
                    // Ubicaciones
                    .requestMatchers("GET", "/api/ruta/ubicaciones").hasAnyRole("ADMIN", "CLIENTE")
                    .requestMatchers("POST", "/api/ruta/ubicaciones").hasAnyRole("ADMIN", "CLIENTE")
                    .requestMatchers("GET", "/api/ruta/ubicaciones/{ubicacionId}").hasAnyRole("ADMIN", "TRANSPORTISTA")
                    
                    // Tarifas Volumen
                    .requestMatchers("GET", "/api/ruta/tarifas/volumen").authenticated()
                    .requestMatchers("POST", "/api/ruta/tarifas/volumen").hasRole("ADMIN")
                    .requestMatchers("PUT", "/api/ruta/tarifas/volumen/{tarifaId}").hasRole("ADMIN")
                    
                    // Tarifas Combustible
                    .requestMatchers("GET", "/api/ruta/tarifas/combustible").authenticated()
                    .requestMatchers("POST", "/api/ruta/tarifas/combustible").hasRole("ADMIN")
                    .requestMatchers("PUT", "/api/ruta/tarifas/combustible/{tarifaId}").hasRole("ADMIN")
                    
                    // Ruta - Estimar costo
                    .requestMatchers("POST", "/api/ruta/estimar-costo").hasAnyRole("ADMIN", "CLIENTE")
                    
                    // Ruta - Calcular definitivo (solo interno desde ms-solicitudes)
                    .requestMatchers("POST", "/api/ruta/calcular-definitivo").hasAnyRole("ADMIN", "TRANSPORTISTA")
                    
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
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            }
            
            return Collections.emptyList();
        }
    }
}