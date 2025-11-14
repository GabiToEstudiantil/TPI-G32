package ar.edu.utn.frc.bda.k7.rutas.dtos;

import lombok.Data;


@Data
public class EstimarCostoRequestDTO {
    private Integer idOrigen;
    private Integer idDestino;
    private Double peso;
    private Double volumen;
}
