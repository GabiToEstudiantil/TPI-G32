package ar.edu.frc.utn.bda.k7.solicitudes.domain;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SOLICITUD")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Solicitud {
    
    private Integer id;
    private LocalDateTime fechaCreacion;
    private Double costoEstimado;
    private String tiempoEstimado;
    private Double costoFinal;
    private String tiempoReal;
    private String clienteDni;
    
    @OneToOne
    @JoinColumn(name = "contenedor_id")
    private Contenedor contenedor;



}
