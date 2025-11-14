package ar.edu.utn.frc.bda.k7.geoapi.dto.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleGeocodingResultDTO {
    
    @JsonProperty("formatted_address")
    private String formatted_address;
    
    // Referencia actualizada a GoogleApiGeometryDTO
    private GoogleApiGeometryDTO geometry;
}
