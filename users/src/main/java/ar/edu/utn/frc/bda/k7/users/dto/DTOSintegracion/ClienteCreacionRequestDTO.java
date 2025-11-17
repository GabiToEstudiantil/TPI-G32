package ar.edu.utn.frc.bda.k7.users.dto.DTOSintegracion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteCreacionRequestDTO {
    
    String nombre;
    String apellido;
    String dni;
    String email;
    String telefono;
    String password; 

}
