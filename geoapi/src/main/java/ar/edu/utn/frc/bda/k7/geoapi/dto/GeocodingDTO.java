package ar.edu.utn.frc.bda.k7.geoapi.dto;

import lombok.Data;

@Data
public class GeocodingDTO {
    private double lat;
    private double lng;
    private String formattedAddress;
}