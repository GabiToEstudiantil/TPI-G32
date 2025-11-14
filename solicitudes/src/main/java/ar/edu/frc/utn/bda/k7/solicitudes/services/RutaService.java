package ar.edu.frc.utn.bda.k7.solicitudes.services;

import org.springframework.stereotype.Service;

import ar.edu.frc.utn.bda.k7.solicitudes.domain.Ruta;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.RutaDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.repositories.RutaRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RutaService {

    private final SolicitudService solicitudService;
    
    private final RutaRepo rutaRepo;

    //Mappers
    public RutaDTO toDto(Ruta ruta) {
        RutaDTO dto = new RutaDTO();
        dto.setId(ruta.getId());
        dto.setCantidadTramos(ruta.getCantidadTramos());
        dto.setCantidadDepositos(ruta.getCantidadDepositos());
        dto.setSolicitud(solicitudService.toDto(ruta.getSolicitud()));
        return dto;
    }

    public Ruta toRuta(RutaDTO dto) {
        Ruta ruta = new Ruta();
        ruta.setId(dto.getId());
        ruta.setCantidadTramos(dto.getCantidadTramos());
        ruta.setCantidadDepositos(dto.getCantidadDepositos());
        ruta.setSolicitud(solicitudService.toSolicitud(dto.getSolicitud()));
        return ruta;
    }
    //FIN Mappers

    public Ruta getRutaById(Integer rutaId) {
        Ruta ruta = rutaRepo.findById(rutaId).orElseThrow(()
            -> new RuntimeException("Ruta no encontrada con id: " + rutaId));
        return ruta;
    }
}
