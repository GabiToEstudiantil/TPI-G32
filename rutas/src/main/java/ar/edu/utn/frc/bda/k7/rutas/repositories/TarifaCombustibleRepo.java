package ar.edu.utn.frc.bda.k7.rutas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.utn.frc.bda.k7.rutas.entities.TarifaCombustible;

public interface TarifaCombustibleRepo extends JpaRepository<TarifaCombustible, Integer> {

    TarifaCombustible findFirstByOrderByIdAsc();
    
}
