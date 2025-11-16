package ar.edu.frc.utn.bda.k7.solicitudes.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "paradas_en_deposito")
public class ParadaEnDeposito {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha_hora_llegada")
    private LocalDateTime fechaHoraLlegada;
    @Column(name = "fecha_hora_salida")
    private LocalDateTime fechaHoraSalida;
    @Column(name = "segundos_estadia")
    private Long segundosEstadia;
    @Column(name = "costo_total_estadia")
    private Double costoTotalEstadia;
    @Column(name = "orden_en_ruta")
    private Integer ordenEnRuta;

    @ManyToOne
    @JoinColumn(name = "ruta_id")
    private Ruta ruta;

    @Column(name = "deposito_id")
    private Integer depositoId;
}
