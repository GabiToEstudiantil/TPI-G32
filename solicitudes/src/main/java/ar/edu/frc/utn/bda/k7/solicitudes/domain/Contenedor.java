package ar.edu.frc.utn.bda.k7.solicitudes.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contenedores")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Contenedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String codigoIdentificacion;
    private Double peso;
    private Double volumen;
    private String estado;
    private String clienteDni;

}
