package ar.edu.utn.frc.bda.k7.geoapi.dto.google;

import lombok.Data;
import java.util.List;

@Data
public class GoogleDistanceRowDTO {
    // Referencia actualizada a GoogleDistanceElementDTO
    private List<GoogleDistanceElementDTO> elements;
}
