package ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos;

import java.util.List;

import ar.edu.frc.utn.bda.k7.solicitudes.dtos.ParadaEnDepositoDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.TramoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalcularDefResponseDTO {
    private Double costoTotal;
    private List<TramoDTO> tramosConCosto;
    private Long segundos;
    private List<ParadaEnDepositoDTO> paradasEnDepositoConCosto;
}
