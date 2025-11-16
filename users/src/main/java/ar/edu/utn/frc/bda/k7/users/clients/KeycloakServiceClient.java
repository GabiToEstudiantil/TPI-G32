// package ar.edu.utn.frc.bda.k7.users.clients;

// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestClient;

// import ar.edu.utn.frc.bda.k7.users.dto.DTOSintegracion.CreateUserRequestDTO;

// import org.springframework.http.ResponseEntity;

// import lombok.AllArgsConstructor;
// import java.net.URI;

// @Service
// @AllArgsConstructor
// public class KeycloakServiceClient {
    
//     private final RestClient keycloakApiClient; 


//     public String crearUsuario(CreateUserRequestDTO request) {
//         String endpoint = "/api/keycloak/users"; 
        
//         try {
//             ResponseEntity<Void> response = keycloakApiClient.post()
//                 .uri(endpoint)
//                 .body(request)
//                 .retrieve()
                
//                 .onStatus(status -> status.isError(), (req, resp) -> {
//                      // Lanza una excepción al encontrar cualquier 4xx o 5xx
//                      throw new RuntimeException("Error en Keycloak al crear usuario. Código HTTP: " + resp.getStatusCode());
//                 })
                
//                 .toBodilessEntity(); // Esperamos 201 Created sin cuerpo
                
//             // Extraer el ID (UUID) del Location Header
//             URI location = response.getHeaders().getLocation();
            
//             if (location != null) {
//                 String path = location.getPath();
//                 return path.substring(path.lastIndexOf('/') + 1);
//             }
            
//             throw new RuntimeException("Keycloak creó el usuario pero no devolvió el ID (Falta Location Header).");

//         } catch (RuntimeException e) {
//             throw e; 
//         } catch (Exception e) {
//             // Maneja fallos de comunicación de red
//             throw new RuntimeException("Fallo de comunicación con el microservicio Keycloak.", e);
//         }
//     }
// }

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