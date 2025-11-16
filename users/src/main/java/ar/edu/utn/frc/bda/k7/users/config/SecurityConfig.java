package ar.edu.utn.frc.bda.k7.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity 
public class SecurityConfig {

    // === 1. WEB SECURITY CUSTOMIZER: IGNORA EL REGISTRO POST ===
    // Esta solución saca la ruta de la cadena de filtros, resolviendo el 401 para el registro.
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            // Ignora el POST al endpoint de creación de clientes.
            .requestMatchers(new AntPathRequestMatcher("/api/users/clientes", HttpMethod.POST.name()));
    }
    
    // === 2. SECURITY FILTER CHAIN: PROTEGE EL RESTO ===
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http
            // 1. Configuración REST
            .csrf(csrf -> csrf.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            
            // 2. Política sin estado (JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 3. Autorización de rutas
            .authorizeHttpRequests(authorize -> authorize
                // Rutas públicas (ej. documentación)
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Todas las demás solicitudes requieren un JWT válido
                .anyRequest().authenticated() 
            )
            
            // 4. Configuración del Resource Server (JWT/Keycloak)
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> {}) 
            );

        return http.build();
    }
}