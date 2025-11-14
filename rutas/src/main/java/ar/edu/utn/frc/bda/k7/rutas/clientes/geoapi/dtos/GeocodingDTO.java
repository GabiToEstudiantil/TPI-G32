package ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeocodingDTO {
    private double lat;
    private double lng;
    private String formattedAddress;
}
