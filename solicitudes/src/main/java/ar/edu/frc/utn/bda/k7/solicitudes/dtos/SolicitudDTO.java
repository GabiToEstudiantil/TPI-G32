package ar.edu.frc.utn.bda.k7.solicitudes.dtos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudDTO {
    private Integer id;
    private LocalDateTime fechaCreacion;
    private Double costoEstimado;
    private String tiempoEstimado;
    private Double costoFinal;
    private String tiempoReal;
    private String clienteDni;
    private ContenedorDTO contenedor; // Usa el DTO de Contenedor
}
