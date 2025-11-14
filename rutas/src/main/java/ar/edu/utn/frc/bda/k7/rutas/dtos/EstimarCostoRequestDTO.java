package ar.edu.utn.frc.bda.k7.rutas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstimarCostoRequestDTO {
    private Integer idOrigen;
    private Integer idDestino;
    private Double peso;
    private Double volumen;
}
