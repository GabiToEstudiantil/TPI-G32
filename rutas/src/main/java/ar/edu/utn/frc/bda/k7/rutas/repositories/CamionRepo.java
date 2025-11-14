package ar.edu.utn.frc.bda.k7.rutas.repositories;

import ar.edu.utn.frc.bda.k7.rutas.entities.Camion;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface CamionRepo extends JpaRepository<Camion, String> {
    
    @Query("SELECT c FROM Camion c WHERE c.disponible = true AND c.capacidadPeso >= :peso AND c.capacidadVolumen >= :volumen")
    List<Camion> findDisponiblesByCapacidad(@Param("peso") Double peso, @Param("volumen") Double volumen);
    
}
