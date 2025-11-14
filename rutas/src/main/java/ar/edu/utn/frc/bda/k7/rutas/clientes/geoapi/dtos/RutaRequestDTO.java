package ar.edu.utn.frc.bda.k7.rutas.dtos.Maps;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutaRequestDTO {
    private String origen;
    private String destino;
    private List<String> paradas;
}
