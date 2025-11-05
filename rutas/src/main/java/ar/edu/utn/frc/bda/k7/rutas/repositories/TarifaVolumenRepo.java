package ar.edu.utn.frc.bda.k7.rutas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.utn.frc.bda.k7.rutas.entities.TarifaVolumen;

public interface TarifaVolumenRepo extends JpaRepository<TarifaVolumen, Integer> {
    // Aca un comentario pq queda feo completamente vacio hasta que le agreguemos alguno personalizado
    
}
