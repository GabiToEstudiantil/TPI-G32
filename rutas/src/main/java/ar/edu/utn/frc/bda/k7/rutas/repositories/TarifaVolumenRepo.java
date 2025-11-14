package ar.edu.utn.frc.bda.k7.rutas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ar.edu.utn.frc.bda.k7.rutas.entities.TarifaVolumen;

public interface TarifaVolumenRepo extends JpaRepository<TarifaVolumen, Integer> {

    @Query("SELECT t FROM TarifaVolumen t WHERE :volumen >= t.volumenMin AND :volumen <= t.volumenMax")
    TarifaVolumen findByVolumen(@Param("volumen") Double volumen);

}
