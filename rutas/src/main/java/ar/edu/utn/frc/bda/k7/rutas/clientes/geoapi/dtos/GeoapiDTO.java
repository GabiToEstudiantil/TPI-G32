package ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoapiDTO {
    private String origen;
    private String destino;
    private double kilometros;
    private String duracionTexto;
    private long duracionSegundos;
}
