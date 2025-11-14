package ar.edu.utn.frc.bda.k7.geoapi.dto.google;

import lombok.Data;
import java.util.List;

// DTO principal para la API Distance Matrix
@Data
public class GoogleDistanceResponseDTO {
    // Referencia actualizada a GoogleDistanceRowDTO
    private List<GoogleDistanceRowDTO> rows;
}
