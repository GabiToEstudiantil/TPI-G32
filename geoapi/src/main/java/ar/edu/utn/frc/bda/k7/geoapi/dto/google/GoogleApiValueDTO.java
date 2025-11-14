package ar.edu.utn.frc.bda.k7.geoapi.dto.google;

import lombok.Data;

// DTO para los objetos {"text": "...", "value": 123}
@Data
public class GoogleApiValueDTO {
    private String text;
    private double value; // Usamos double para distancia (metros) y duración (segundos)
}