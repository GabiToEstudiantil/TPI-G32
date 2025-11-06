package ar.edu.utn.frc.bda.k7.rutas.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tarifas_volumen")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TarifaVolumen {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "volumen_min")
    private Double volumenMin;

    @Column(name = "volumen_max")
    private Double volumenMax;

    @Column(name = "costo_km_base")
    private Double costoKmBase;

}
