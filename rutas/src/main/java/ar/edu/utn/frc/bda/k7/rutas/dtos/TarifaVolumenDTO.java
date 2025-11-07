package ar.edu.utn.frc.bda.k7.rutas.dtos;

import lombok.Data;

@Data
public class TarifaVolumenDTO {
    private Integer id;
    private Double volumenMin;
    private Double volumenMax;
    private Double costoKmBase;
}
