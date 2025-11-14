package ar.edu.frc.utn.bda.k7.solicitudes.services;

import java.util.ArrayList;
import org.springframework.stereotype.Service;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Solicitud;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.SolicitudDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.repositories.SolicitudRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SolicitudService {

    private final ContenedorService contenedorService;
    
    private final SolicitudRepo solicitudRepo;

    //Mappers
    public SolicitudDTO toDto(Solicitud solicitud) {
        SolicitudDTO dto = new SolicitudDTO();
        dto.setId(solicitud.getId());
        dto.setFechaCreacion(solicitud.getFechaCreacion());
        dto.setTiempoEstimado(solicitud.getTiempoEstimado());
        dto.setCostoEstimado(solicitud.getCostoEstimado());
        dto.setTiempoReal(solicitud.getTiempoReal());
        dto.setCostoFinal(solicitud.getCostoFinal());
        dto.setClienteDni(solicitud.getClienteDni());
        dto.setContenedor(contenedorService.toDto(solicitud.getContenedor()));
        return dto;
    }

    public Solicitud toSolicitud(SolicitudDTO dto) {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(dto.getId());
        solicitud.setFechaCreacion(dto.getFechaCreacion());
        solicitud.setTiempoEstimado(dto.getTiempoEstimado());
        solicitud.setCostoEstimado(dto.getCostoEstimado());
        solicitud.setTiempoReal(dto.getTiempoReal());
        solicitud.setCostoFinal(dto.getCostoFinal());
        solicitud.setClienteDni(dto.getClienteDni());
        solicitud.setContenedor(contenedorService.toContenedor(dto.getContenedor()));
        return solicitud;
    }
    //FIN mappers

    public ArrayList<SolicitudDTO> obtenerTodas(){
        ArrayList<SolicitudDTO> solicitudDTOs = new ArrayList<>();
        for (Solicitud solicitud : solicitudRepo.findAll()){
            solicitudDTOs.add(toDto(solicitud));
        }
        return solicitudDTOs;
    }

    public SolicitudDTO buscarPorId(Integer id){
        return toDto(solicitudRepo.findById(id).orElse(null));
    }

    public ArrayList<SolicitudDTO> obtenerPorDniCliente(String dni){
        ArrayList<SolicitudDTO> solicitudDTOs = new ArrayList<>();
        for (Solicitud solicitud : solicitudRepo.findByClienteDni(dni)){
            solicitudDTOs.add(toDto(solicitud));
        }
        return solicitudDTOs;
    }
}
