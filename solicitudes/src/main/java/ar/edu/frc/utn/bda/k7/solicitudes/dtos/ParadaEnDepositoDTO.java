package ar.edu.frc.utn.bda.k7.solicitudes.dtos;

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
    private Integer diasEstadia;
    private Double costoTotalEstadia;
    private Integer ordenEnRuta;
    private Integer depositoId;
    
    private Integer rutaId;
}
