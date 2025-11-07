package ar.edu.utn.frc.bda.k7.rutas.dtos;

import lombok.Data;

@Data
public class TarifaCombustibleDTO {
    private Integer id;
    private Double precioLitro;
    private String nombre;
}
