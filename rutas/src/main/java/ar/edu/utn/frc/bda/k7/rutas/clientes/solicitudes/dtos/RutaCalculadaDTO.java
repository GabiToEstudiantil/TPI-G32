package ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos;

import java.util.List;

// import ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.dtos.GeoapiDTO; // Ya no se usa
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutaCalculadaDTO {
    
    private List<TramoCalcularRutaDTO> tramos;
    
    private Double costoEstimado;

}
