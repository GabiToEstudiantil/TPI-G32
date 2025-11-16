package ar.edu.utn.frc.bda.k7.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    String dni;
    String nombre;
    String apellido;
    String email;
    String telefono;
    String keycloak_id;

}



