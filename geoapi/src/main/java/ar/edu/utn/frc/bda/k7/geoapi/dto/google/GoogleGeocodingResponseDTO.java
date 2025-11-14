package ar.edu.utn.frc.bda.k7.geoapi.dto.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

// DTO principal para la API Geocoding
@Data
public class GoogleGeocodingResponseDTO {
    
    // Referencia actualizada a GoogleGeocodingResultDTO
    private List<GoogleGeocodingResultDTO> results;
    private String status;

    @JsonProperty("error_message")
    private String error_message;
}
