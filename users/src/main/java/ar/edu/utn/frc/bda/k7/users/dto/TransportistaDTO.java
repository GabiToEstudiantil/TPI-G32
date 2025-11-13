package ar.edu.utn.frc.bda.k7.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransportistaDTO {

    Integer legajo;
    String nombre;
    String apellido;
    Integer dni;
    String email;
    String telefono;
    Integer keycloak_id;
    
}
