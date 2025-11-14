package ar.edu.utn.frc.bda.k7.rutas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositoDTO {
    private Integer id;
    private String nombre;
    private Double costoEstadiaDiario;
    private UbicacionDTO ubicacion;
}
