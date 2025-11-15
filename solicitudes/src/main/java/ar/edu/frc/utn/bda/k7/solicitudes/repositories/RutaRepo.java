package ar.edu.frc.utn.bda.k7.solicitudes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Ruta;

public interface RutaRepo extends JpaRepository<Ruta, Integer> {

    Ruta findBySolicitudId(Integer solicitudId);
}
