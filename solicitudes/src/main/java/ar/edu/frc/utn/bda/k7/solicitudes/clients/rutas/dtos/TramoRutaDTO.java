package ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TramoRutaDTO {
    private GeoapiDTO rutaDelTramo;
    private CamionDTO camionAsignado;
    private DepositoDTO depositoParada;
    private Double costoEstimado;
}