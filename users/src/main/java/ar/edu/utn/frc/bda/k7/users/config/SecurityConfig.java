package ar.edu.utn.frc.bda.k7.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Deshabilita CSRF
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/actuator/health", "/actuator/info").permitAll() // Permite health checks
                    .anyRequest().authenticated() // Requiere autenticación para todo lo demás
            )
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(withDefaults()) // Configura para validar tokens JWT
            );

        return http.build();
    }
}