package ar.edu.frc.utn.bda.k7.solicitudes.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.frc.utn.bda.k7.solicitudes.domain.Ruta;
import ar.edu.frc.utn.bda.k7.solicitudes.repositories.RutaRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RutaService {

    
    private final RutaRepo rutaRepo;

    public Ruta getRutaById(Integer rutaId) {
        Ruta ruta = rutaRepo.findById(rutaId).orElseThrow(()
            -> new RuntimeException("Ruta no encontrada con id: " + rutaId));
        return ruta;
    }

    @Transactional
    public Ruta save(Ruta ruta) {
        return rutaRepo.save(ruta);
    }

    public Ruta getRutaBySolicitudId(Integer solicitudId) {
        return rutaRepo.findBySolicitudId(solicitudId);
    }
}
