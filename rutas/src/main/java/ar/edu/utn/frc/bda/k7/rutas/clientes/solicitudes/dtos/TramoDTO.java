package ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TramoDTO {
    private Integer id;
    private Integer origenId;
    private Integer destinoId;
    
    private TramoTipo tipo;
    private TramoEstado estado;

    private Double distanciaKm;
    private Double costoAproximado;
    private Double costoReal;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String camionDominio;
    
    private Integer rutaId;
}
