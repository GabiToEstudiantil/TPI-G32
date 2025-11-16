package ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos;

import java.util.List;

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
