package ar.edu.frc.utn.bda.k7.solicitudes.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.frc.utn.bda.k7.solicitudes.domain.Ruta;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Tramo;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.TramoEstado;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.TramoDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.TramoEstadoPatchDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.repositories.TramoRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TramoService {
    
    private final TramoRepo tramoRepo;
    private final RutaService rutaService;

    //Mappers
    public TramoDTO toDto(Tramo tramo) {
        TramoDTO dto = new TramoDTO();
        dto.setId(tramo.getId());
        dto.setOrigenId(tramo.getOrigenId());
        dto.setDestinoId(tramo.getDestinoId());
        dto.setDistanciaKm(tramo.getDistanciaKm());
        dto.setTipo(tramo.getTipo());
        dto.setEstado(tramo.getEstado());
        dto.setDistanciaKm(tramo.getDistanciaKm());
        dto.setCostoAproximado(tramo.getCostoAproximado());
        dto.setCostoReal(tramo.getCostoReal());
        dto.setFechaHoraInicio(tramo.getFechaHoraInicio());
        dto.setFechaHoraFin(tramo.getFechaHoraFin());
        dto.setCamionDominio(tramo.getCamionDominio());
        dto.setRutaId(tramo.getRuta().getId());
        return dto;
    }

    public Tramo toTramo(TramoDTO dto) {
        Tramo tramo = new Tramo();
        tramo.setId(dto.getId());
        tramo.setOrigenId(dto.getOrigenId());
        tramo.setDestinoId(dto.getDestinoId());
        tramo.setDistanciaKm(dto.getDistanciaKm());
        tramo.setTipo(dto.getTipo());
        tramo.setEstado(dto.getEstado());
        tramo.setDistanciaKm(dto.getDistanciaKm());
        tramo.setCostoAproximado(dto.getCostoAproximado());
        tramo.setCostoReal(dto.getCostoReal());
        tramo.setFechaHoraInicio(dto.getFechaHoraInicio());
        tramo.setFechaHoraFin(dto.getFechaHoraFin());
        tramo.setCamionDominio(dto.getCamionDominio());
        tramo.setRuta(rutaService.getRutaById(dto.getRutaId()));
        return tramo;
    }
    //FIN Mappers

    @Transactional
    public Tramo save(Tramo tramo) {
        return tramoRepo.save(tramo);
    }

    public List<Tramo> getTramosByRutaOrdenados(Ruta ruta) {
        return tramoRepo.findByRutaOrderByOrdenEnRutaAsc(ruta);
    }

    public Tramo getTramoById(Integer tramoId) {
        return tramoRepo.findById(tramoId).orElseThrow(()
            -> new RuntimeException("Tramo no encontrado con id: " + tramoId));
    }

    public TramoDTO actualizarEstado(Integer tramoId, TramoEstadoPatchDTO tramoEstadoPatchDTO) {
        Tramo tramo = getTramoById(tramoId);
        tramo.setEstado(tramoEstadoPatchDTO.getNuevoEstado());
        Tramo tramoActualizado = save(tramo);
        return toDto(tramoActualizado);
    }

}
