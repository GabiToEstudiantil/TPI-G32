package ar.edu.frc.utn.bda.k7.solicitudes.dtos;

import ar.edu.frc.utn.bda.k7.solicitudes.domain.TramoEstado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TramoEstadoPatchDTO {
    private String dominioCamion;
    private TramoEstado nuevoEstado;
}
