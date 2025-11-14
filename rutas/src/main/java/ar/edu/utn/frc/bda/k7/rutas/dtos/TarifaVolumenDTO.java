package ar.edu.utn.frc.bda.k7.rutas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TarifaVolumenDTO {
    private Integer id;
    private Double volumenMin;
    private Double volumenMax;
    private Double costoKmBase;
}
