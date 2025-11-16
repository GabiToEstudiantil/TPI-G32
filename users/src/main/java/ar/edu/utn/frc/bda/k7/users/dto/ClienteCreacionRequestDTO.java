package ar.edu.utn.frc.bda.k7.users.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
// Heredamos los campos del DTO de respuesta y añadimos la contraseña
@EqualsAndHashCode(callSuper = true)
public class ClienteCreacionRequestDTO extends ClienteDTO {
    
    private String password; 
    
    // El resto de campos (dni, nombre, apellido, email, telefono) 
    // se heredan de ClienteDTO.
}

