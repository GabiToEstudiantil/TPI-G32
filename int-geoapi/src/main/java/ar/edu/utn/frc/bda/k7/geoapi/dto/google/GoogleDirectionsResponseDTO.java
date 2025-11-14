package ar.edu.utn.frc.bda.k7.geoapi.dto.google;

import lombok.Data;
import java.util.List;

// DTO principal para la API Directions
@Data
public class GoogleDirectionsResponseDTO {
    // Referencia actualizada a GoogleDirectionsRouteDTO
    private List<GoogleDirectionsRouteDTO> routes;
}
