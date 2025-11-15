package ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.EstimarCostoRequestDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.RutaCalculadaDTO;

import org.springframework.http.MediaType;

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
                    .body(request)
                    .retrieve()
                    .body(RutaCalculadaDTO.class);
        } catch (Exception e) {
            System.err.println("Error al llamar a ms-rutas: " + e.getMessage());
            return null;
        }
    }
}