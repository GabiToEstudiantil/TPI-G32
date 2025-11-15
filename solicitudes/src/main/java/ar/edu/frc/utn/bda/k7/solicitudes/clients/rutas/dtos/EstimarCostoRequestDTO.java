package ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstimarCostoRequestDTO {
    private Integer idOrigen;
    private Integer idDestino;
    private Double peso;
    private Double volumen;
}
