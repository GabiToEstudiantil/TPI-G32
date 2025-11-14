package ar.edu.frc.utn.bda.k7.solicitudes.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Solicitud;

public interface SolicitudRepo extends JpaRepository<Solicitud, Integer> {

    List<Solicitud> findByClienteDni(String dni);
}
