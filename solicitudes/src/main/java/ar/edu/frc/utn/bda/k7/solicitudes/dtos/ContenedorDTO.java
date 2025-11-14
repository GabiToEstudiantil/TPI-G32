package ar.edu.frc.utn.bda.k7.solicitudes.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContenedorDTO {
    private Integer id;
    private String codigoIdentificacion;
    private Double peso;
    private Double volumen;
    private String estado;
    private String clienteDni;
}
