package ar.edu.utn.frc.bda.k7.geoapi.dto;

import lombok.Data;

@Data
public class GeoapiDTO {
    private String origen;
    private String destino;
    private double kilometros;
    private String duracionTexto;
    private long duracionSegundos;
}
