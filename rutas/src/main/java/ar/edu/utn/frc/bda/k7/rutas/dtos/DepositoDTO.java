package ar.edu.utn.frc.bda.k7.rutas.dtos;

import lombok.Data;

@Data
public class DepositoDTO {
    private Integer id;
    private String nombre;
    private Double costoEstadiaDiario;
    private UbicacionDTO ubicacion;
}
