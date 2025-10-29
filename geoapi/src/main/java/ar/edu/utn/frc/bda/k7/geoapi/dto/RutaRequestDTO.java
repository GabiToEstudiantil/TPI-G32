package ar.edu.utn.frc.bda.k7.geoapi.dto;

import java.util.List;
import lombok.Data;

@Data
public class RutaRequestDTO {
    private String origen;      // Dirección de inicio
    private String destino;     // Dirección final
    private List<String> paradas; // Lista de direcciones de paradas intermedias
}
