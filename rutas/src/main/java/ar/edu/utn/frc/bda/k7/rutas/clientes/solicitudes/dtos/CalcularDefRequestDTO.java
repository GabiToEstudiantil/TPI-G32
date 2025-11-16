package ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalcularDefRequestDTO {
    private List<TramoDTO> tramos;
    private List<ParadaEnDepositoDTO> paradasEnDeposito;
}
