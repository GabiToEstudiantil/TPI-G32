package ar.edu.frc.utn.bda.k7.solicitudes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.frc.utn.bda.k7.solicitudes.domain.Tramo;

public interface TramoRepo extends JpaRepository<Tramo, Integer> {
    // Aca un comentario pq queda feo completamente vacio hasta que le agreguemos alguno personalizado
    
}
