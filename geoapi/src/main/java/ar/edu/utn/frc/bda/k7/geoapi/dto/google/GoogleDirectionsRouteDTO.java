package ar.edu.utn.frc.bda.k7.geoapi.dto.google;

import lombok.Data;
import java.util.List;

@Data
public class GoogleDirectionsRouteDTO {
    // Referencia actualizada a GoogleDirectionsLegDTO
    private List<GoogleDirectionsLegDTO> legs;
}
