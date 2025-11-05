package ar.edu.utn.frc.bda.k7.rutas.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "camiones")
@Entity
public class Camion {

    @Id @Column(name = "dominio")
    private String dominio;

    @Column(name = "transportista_legajo")
    private String transportistaLegajo;

    @Column(name = "capacidad_peso")
    private Double capacidadPeso;

    @Column(name = "capacidad_volumen")
    private Double capacidadVolumen;

    @Column(name = "disponible")
    private boolean disponible;

    @Column(name = "consumo_combustible_promedio")
    private Double consumoCombustiblePromedio;
}
