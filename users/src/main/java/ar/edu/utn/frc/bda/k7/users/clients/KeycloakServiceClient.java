

package ar.edu.utn.frc.bda.k7.users.clients;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ar.edu.utn.frc.bda.k7.users.dto.DTOSintegracion.CreateUserRequestDTO;
import lombok.AllArgsConstructor;
import java.net.URI;

@Service
@AllArgsConstructor
public class KeycloakServiceClient {
    
    private final RestTemplate restTemplate; // Cliente HTTP simple

    public String crearUsuario(CreateUserRequestDTO request) {
        try {
            // URL completa al microservicio / gateway / keycloak-proxy
            String url = "http://keycloak:8080/api/keycloak/users";

            // Envia POST y obtiene el LOCATION en la respuesta (201 Created)
            URI location = restTemplate.postForLocation(url, request);

            if (location != null) {
                // Ejemplo: /api/keycloak/users/54ddb931-8ff3-4abc-9e1b-341acbf9f83b
                String path = location.getPath();
                return path.substring(path.lastIndexOf('/') + 1);
            }

            throw new RuntimeException("Keycloak creó el usuario pero no devolvió Location con el ID");

        } catch (Exception e) {
            throw new RuntimeException("Error comunicándose con Keycloak", e);
        }
    }
}
