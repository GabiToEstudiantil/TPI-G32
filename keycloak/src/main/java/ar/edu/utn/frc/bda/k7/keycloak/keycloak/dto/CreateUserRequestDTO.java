package ar.edu.utn.frc.bda.k7.keycloak.keycloak.dto;

import lombok.Data;

@Data
public class CreateUserRequestDTO {
    private String username;
    private String email;
    private String password;
    private String role;
}
