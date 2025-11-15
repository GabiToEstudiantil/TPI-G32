package ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CamionDTO {
    private String dominio;
    private String transportistaLegajo;
    private Double capacidadPeso;
    private Double capacidadVolumen;
    private boolean disponible;
    private Double consumoCombustiblePromedio;
}