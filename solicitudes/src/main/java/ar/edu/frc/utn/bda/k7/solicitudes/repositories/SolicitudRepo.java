package ar.edu.frc.utn.bda.k7.solicitudes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.frc.utn.bda.k7.solicitudes.domain.Solicitud;

public interface SolicitudRepo extends JpaRepository<Solicitud, Integer> {
    // Aca un comentario pq queda feo completamente vacio hasta que le agreguemos alguno personalizado
}
