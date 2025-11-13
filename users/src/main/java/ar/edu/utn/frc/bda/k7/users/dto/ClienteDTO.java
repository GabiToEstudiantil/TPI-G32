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

    Integer keycloak_id;
    Integer dni;
    String nombre;
    String apellido;
    String email;
    String telefono;

}

//podriamos hacer un dto para request y otro para response

