package ar.edu.utn.frc.bda.k7.rutas.dtos.Maps;

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
