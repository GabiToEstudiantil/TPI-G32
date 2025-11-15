package ar.edu.frc.utn.bda.k7.solicitudes.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.frc.utn.bda.k7.solicitudes.domain.Ruta;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Tramo;

public interface TramoRepo extends JpaRepository<Tramo, Integer> {
    
    List<Tramo> findByRutaOrderByOrdenEnRutaAsc(Ruta ruta);
}
