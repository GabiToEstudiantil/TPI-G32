// package ar.edu.utn.frc.bda.k7.users.clients;

// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestClient;

// import ar.edu.utn.frc.bda.k7.users.dto.CreateUserRequestDTO; 

// @Service
// public class KeycloakServiceClient {

//     private final RestClient keycloakApiClient; 

//     public KeycloakServiceClient(RestClient keycloakApiClient) {
//         this.keycloakApiClient = keycloakApiClient;
//     }

//     public void crearUsuario(CreateUserRequestDTO request) {
//         String endpoint = "/api/keycloak/users"; 
        
//         try {
//             keycloakApiClient.post()
//                 .uri(endpoint)
//                 .body(request)
//                 .retrieve()
//                 .toBodilessEntity(); // Esperamos 201 Created (o similar) sin cuerpo
//         } catch (Exception e) {
//             // Manejo de errores si Keycloak falla (ej: usuario ya existe)
//             throw new RuntimeException("Fallo al crear el usuario en Keycloak: " + e.getMessage(), e);
//         }
//     }
// }
package ar.edu.utn.frc.bda.k7.users.clients;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import ar.edu.utn.frc.bda.k7.users.dto.CreateUserRequestDTO; // Ajusta este paquete si es necesario
import lombok.AllArgsConstructor;

import java.net.URI;
// Importaciones necesarias:
// import org.springframework.web.client.RestClient;
// import org.springframework.http.HttpStatus; 
// ...

@Service
@AllArgsConstructor
public class KeycloakServiceClient {
    
    private final RestClient keycloakApiClient; 

    /**
     * Llama al microservicio keycloak para crear un usuario.
     * @param request Datos del usuario a crear.
     * @return El ID (UUID) del usuario creado en Keycloak como String.
     */
    public String crearUsuario(CreateUserRequestDTO request) {
        String endpoint = "/api/keycloak/users"; 
        
        try {
            ResponseEntity<Void> response = keycloakApiClient.post()
                .uri(endpoint)
                .body(request)
                .retrieve()
                
                // === CÓDIGO CORREGIDO ===
                // Usamos una lambda explícita: 'status -> status.isError()'
                .onStatus(status -> status.isError(), (req, resp) -> {
                     // Lanza una excepción al encontrar cualquier 4xx o 5xx
                     throw new RuntimeException("Error en Keycloak al crear usuario. Código HTTP: " + resp.getStatusCode());
                })
                // =========================
                
                .toBodilessEntity(); // Esperamos 201 Created sin cuerpo
                
            // Extraer el ID (UUID) del Location Header
            URI location = response.getHeaders().getLocation();
            
            if (location != null) {
                // El ID es el último segmento de la URI
                String path = location.getPath();
                return path.substring(path.lastIndexOf('/') + 1);
            }
            
            throw new RuntimeException("Keycloak creó el usuario pero no devolvió el ID (Falta Location Header).");

        } catch (RuntimeException e) {
            // Relanza excepciones generadas por .onStatus
            throw e; 
        } catch (Exception e) {
            // Maneja fallos de comunicación de red
            throw new RuntimeException("Fallo de comunicación con el microservicio Keycloak.", e);
        }
    }
}