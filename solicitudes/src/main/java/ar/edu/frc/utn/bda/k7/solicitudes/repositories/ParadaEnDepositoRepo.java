package ar.edu.frc.utn.bda.k7.solicitudes.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.ParadaEnDeposito;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Ruta;

public interface ParadaEnDepositoRepo extends JpaRepository<ParadaEnDeposito, Integer> {
    ParadaEnDeposito findByRutaIdAndOrdenEnRuta(Integer rutaId, Integer ordenEnRuta);

    List<ParadaEnDeposito> findByRutaId(Integer rutaId);
}
