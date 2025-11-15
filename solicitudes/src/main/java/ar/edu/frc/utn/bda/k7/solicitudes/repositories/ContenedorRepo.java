package ar.edu.frc.utn.bda.k7.solicitudes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Contenedor;

public interface ContenedorRepo extends JpaRepository<Contenedor, Integer> {
    
    Contenedor findByCodigoIdentificacion(String codigo);
}
