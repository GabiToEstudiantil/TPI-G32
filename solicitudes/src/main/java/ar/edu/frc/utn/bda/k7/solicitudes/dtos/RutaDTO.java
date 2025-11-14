package ar.edu.frc.utn.bda.k7.solicitudes.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RutaDTO {
    private Integer id;
    private Integer cantidadTramos;
    private Integer cantidadDepositos;
    private SolicitudDTO solicitud;
}
