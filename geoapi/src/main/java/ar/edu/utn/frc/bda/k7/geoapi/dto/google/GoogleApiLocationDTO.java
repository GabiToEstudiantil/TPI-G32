package ar.edu.utn.frc.bda.k7.geoapi.dto.google;

import lombok.Data;

// DTO para los objetos {"lat": 1.23, "lng": 4.56}
@Data
public class GoogleApiLocationDTO {
    private double lat;
    private double lng;
}
