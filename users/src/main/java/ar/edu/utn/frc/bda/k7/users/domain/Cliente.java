package ar.edu.utn.frc.bda.k7.users.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    private String dni;

    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String keycloak_id;
}


