package ar.edu.frc.utn.bda.k7.solicitudes.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solicitudes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Solicitud {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    @Column(name = "costo_estimado")
    private Double costoEstimado;
    @Column(name = "tiempo_estimado")
    private String tiempoEstimado;
    @Column(name = "costo_final")
    private Double costoFinal;
    @Column(name = "tiempo_real")
    private String tiempoReal;
    @Column(name = "cliente_dni")
    private String clienteDni;
    
    @OneToOne
    @JoinColumn(name = "contenedor_id")
    private Contenedor contenedor;

}
