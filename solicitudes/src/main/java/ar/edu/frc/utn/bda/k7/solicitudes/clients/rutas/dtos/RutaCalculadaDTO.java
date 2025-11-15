package ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RutaCalculadaDTO {
    private List<TramoRutaDTO> tramos;
    private Double costoEstimado;
}
