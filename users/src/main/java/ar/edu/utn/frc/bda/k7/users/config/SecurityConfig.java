package ar.edu.utn.frc.bda.k7.users.config;

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
                    
                    // Clientes
                    .requestMatchers("POST", "/api/users/clientes").permitAll() // Crear cliente sin autenticación
                    .requestMatchers("GET", "/api/users/clientes").hasRole("ADMIN") // Listar todos solo ADMIN
                    .requestMatchers("GET", "/api/users/clientes/{dni}").hasAnyRole("ADMIN", "TRANSPORTISTA", "CLIENTE") // Ver un cliente
                    .requestMatchers("PUT", "/api/users/clientes/{dni}").hasAnyRole("ADMIN", "CLIENTE") // Actualizar cliente
                    
                    // Transportistas
                    .requestMatchers("POST", "/api/users/transportistas").permitAll() // Crear transportista sin autenticación
                    .requestMatchers("GET", "/api/users/transportistas").hasRole("ADMIN") // Listar todos solo ADMIN
                    .requestMatchers("GET", "/api/users/transportistas/{legajo}").authenticated() // Ver un transportista (cualquiera autenticado)
                    .requestMatchers("PUT", "/api/users/transportistas/{legajo}").hasAnyRole("ADMIN", "TRANSPORTISTA") // Actualizar transportista
                    
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

