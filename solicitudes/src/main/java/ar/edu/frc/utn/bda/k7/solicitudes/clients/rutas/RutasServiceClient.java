package ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.CalcularDefRequestDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.CalcularDefResponseDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.EstimarCostoRequestDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.RutaCalculadaDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.UbicacionDTO;

@Service
public class RutasServiceClient {

    private final RestClient restClient;

    public RutasServiceClient(RestClient rutasApiClient) {
        this.restClient = rutasApiClient;
    }

    public RutaCalculadaDTO estimarCosto(EstimarCostoRequestDTO request) {
        try {
            return restClient.post()
                    .uri("/api/ruta/estimar-costo")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + getBearerToken()) // ← AGREGAR TOKEN
                    .body(request)
                    .retrieve()
                    .body(RutaCalculadaDTO.class);
        } catch (Exception e) {
            System.err.println("Error al llamar a ms-rutas: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public UbicacionDTO getUbicacionById(Integer ubicacionId) {
        try {
            return restClient.get()
                    .uri("/api/ruta/ubicaciones/{id}", ubicacionId)
                    .header("Authorization", "Bearer " + getBearerToken()) // ← AGREGAR TOKEN
                    .retrieve()
                    .body(UbicacionDTO.class);
        } catch (Exception e) {
            System.err.println("Error al llamar a ms-rutas para obtener ubicacion: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public CalcularDefResponseDTO calcularCostoDefinitivo(CalcularDefRequestDTO request) {
        try {
            return restClient.post()
                    .uri("/api/ruta/calcular-definitivo")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + getBearerToken()) // ← AGREGAR TOKEN
                    .body(request)
                    .retrieve()
                    .body(CalcularDefResponseDTO.class);
        } catch (Exception e) {
            System.err.println("Error al llamar a ms-rutas para calcular costo definitivo: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private String getBearerToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getTokenValue();
        }
        
        throw new RuntimeException("No se encontró el token JWT en el contexto de seguridad");
    }
}