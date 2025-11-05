package ar.edu.utn.frc.bda.k7.rutas.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ubicaciones")
public class Ubicacion {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @Column(name = "direccion_textual")
    private String direccionTextual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Ciudad ciudad;

}
