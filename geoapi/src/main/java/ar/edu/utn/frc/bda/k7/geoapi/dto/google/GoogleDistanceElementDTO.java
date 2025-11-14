package ar.edu.utn.frc.bda.k7.geoapi.dto.google;

import lombok.Data;

@Data
public class GoogleDistanceElementDTO {
    // Referencia actualizada a GoogleApiValueDTO
    private GoogleApiValueDTO distance;
    private GoogleApiValueDTO duration;
}
