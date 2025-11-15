package ar.edu.frc.utn.bda.k7.solicitudes.dtos;

import ar.edu.frc.utn.bda.k7.solicitudes.domain.SolicitudEstado; // Crearemos este enum
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingDTO {

    // Resumen de la solicitud
    private Integer solicitudId;
    private SolicitudEstado estadoGeneral; // Ej: EN_TRANSITO

    // Info del tramo actual
    private String descripcionUbicacion; // Ej: "En viaje de Buenos Aires a Depósito Rosario"
    private String camionAsignado;
    
    // Progreso
    private int tramosCompletados;
    private int tramosTotales;
}
