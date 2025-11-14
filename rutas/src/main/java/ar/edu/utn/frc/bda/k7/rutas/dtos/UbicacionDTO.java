package ar.edu.utn.frc.bda.k7.rutas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionDTO {
    private Integer id;
    private Double latitud;
    private Double longitud;
    private String direccionTextual;
    private CiudadDTO ciudad;
}
