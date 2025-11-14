package ar.edu.utn.frc.bda.k7.rutas.dtos;

import ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.dtos.GeoapiDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TramoDTO {
    
    private GeoapiDTO rutaDelTramo;

    private CamionDTO camionAsignado;

    private DepositoDTO depositoParada;
}