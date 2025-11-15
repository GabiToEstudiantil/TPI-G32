package ar.edu.frc.utn.bda.k7.solicitudes.dtos;

import ar.edu.frc.utn.bda.k7.solicitudes.domain.ContenedorEstado;
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
    private ContenedorEstado estado;
    private String clienteDni;
}
