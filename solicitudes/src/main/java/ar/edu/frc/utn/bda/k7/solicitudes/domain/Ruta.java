package ar.edu.frc.utn.bda.k7.solicitudes.domain;

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
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "rutas")
public class Ruta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cantidad_tramos")
    private Integer cantidadTramos;
    @Column(name = "cantidad_depositos")
    private Integer cantidadDepositos;

    @OneToOne
    @JoinColumn(name = "solicitud_id")
    private Solicitud solicitud;
}
