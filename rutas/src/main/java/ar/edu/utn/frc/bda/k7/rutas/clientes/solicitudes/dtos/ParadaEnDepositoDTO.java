package ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParadaEnDepositoDTO {
    private Integer id;
    private LocalDateTime fechaHoraLlegada;
    private LocalDateTime fechaHoraSalida;
    private Long segundosEstadia;
    private Double costoTotalEstadia;
    private Integer ordenEnRuta;
    private Integer depositoId;
    private Integer rutaId;
}
