package ar.edu.utn.frc.bda.k7.rutas.dtos.Maps;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GeocodingDTO {
    private double lat;
    private double lng;
    private String formattedAddress;
}
