package ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos;

import ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.dtos.GeoapiDTO;
import ar.edu.utn.frc.bda.k7.rutas.dtos.CamionDTO;
import ar.edu.utn.frc.bda.k7.rutas.dtos.DepositoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TramoCalcularRutaDTO {
    
    private GeoapiDTO rutaDelTramo;

    private CamionDTO camionAsignado;

    private DepositoDTO depositoParada;

    private Double costoEstimado;
}