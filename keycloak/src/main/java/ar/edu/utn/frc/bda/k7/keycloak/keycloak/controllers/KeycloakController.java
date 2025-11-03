package ar.edu.utn.frc.bda.k7.keycloak.keycloak.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;;;

@RestController
@Slf4j
public class KeycloakController {

// 1. LA RUTA DEBE COINCIDIR CON LA DEL GATEWAY
    @GetMapping("/api/auth/oauth2/code/keycloak")
    public String intercambiarCode(@RequestParam String code) throws UnsupportedEncodingException {

        log.info("CÓDIGO RECIBIDO EN 9091: {}", code);

        RestClient restClient = RestClient.create();

// 2. LA REDIRECT_URI TAMBIÉN DEBE ACTUALIZARSE AQUÍ
// ...
        String formData = "grant_type=authorization_code" +
                "&code=" + URLEncoder.encode(code, StandardCharsets.UTF_8) +
                "&client_id=tpi-backend-client" +
                "&redirect_uri=" + 
                // ¡Esta es la correcta! La dirección PÚBLICA de tu Gateway
                URLEncoder.encode("http://localhost:9191/api/auth/oauth2/code/keycloak", StandardCharsets.UTF_8); // <-- BIEN
        // ...

        String token = restClient.post()
                .uri("http://localhost:8081/realms/tpi-backend/protocol/openid-connect/token") // [cite: 500-502]
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(String.class);

        log.info("TOKEN OBTENIDO: {}", token); // [cite: 506-508]

        return "¡Token recibido! Revisa la consola de tu servicio 'keycloak' (el que corre en 9091)";
    }
}
