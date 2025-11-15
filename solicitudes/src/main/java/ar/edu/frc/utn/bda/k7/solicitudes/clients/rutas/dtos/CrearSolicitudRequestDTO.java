package ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearSolicitudRequestDTO {
    
    // Info para la Solicitud
    private String clienteDni;
    
    // Info para el Contenedor
    private String codigoContenedor;
    private Double peso;
    private Double volumen;
    
    // Info para la Ruta
    private Integer origenId;
    private Integer destinoId;
}
