package ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GeoapiDTO {
    private String origen;
    private String destino;
    private double kilometros;
    private String duracionTexto;
    private long duracionSegundos;
}
