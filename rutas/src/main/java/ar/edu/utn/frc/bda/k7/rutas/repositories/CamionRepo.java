package ar.edu.utn.frc.bda.k7.rutas.repositories;

import ar.edu.utn.frc.bda.k7.rutas.entities.Camion;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface CamionRepo extends JpaRepository<Camion, String> {
    // Aca un comentario pq queda feo completamente vacio hasta que le agreguemos alguno personalizado
    
}
