package ar.edu.frc.utn.bda.k7.solicitudes.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.frc.utn.bda.k7.solicitudes.domain.ParadaEnDeposito;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.ParadaEnDepositoDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.repositories.ParadaEnDepositoRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParadaEnDepositoService {

    private final RutaService rutaService;
    
    private final ParadaEnDepositoRepo paradaRepo;

    //Mappers
    public ParadaEnDepositoDTO toDto(ParadaEnDeposito parada) {
        ParadaEnDepositoDTO dto = new ParadaEnDepositoDTO();
        dto.setId(parada.getId());
        dto.setFechaHoraLlegada(parada.getFechaHoraLlegada());
        dto.setFechaHoraSalida(parada.getFechaHoraSalida());
        dto.setDiasEstadia(parada.getDiasEstadia());
        dto.setCostoTotalEstadia(parada.getCostoTotalEstadia());
        dto.setOrdenEnRuta(parada.getOrdenEnRuta());
        dto.setRutaId(parada.getRuta().getId());
        dto.setDepositoId(parada.getDepositoId());
        return dto;
    }

    public ParadaEnDeposito toParada(ParadaEnDepositoDTO dto) {
        ParadaEnDeposito parada = new ParadaEnDeposito();
        parada.setId(dto.getId());
        parada.setFechaHoraLlegada(dto.getFechaHoraLlegada());
        parada.setFechaHoraSalida(dto.getFechaHoraSalida());
        parada.setDiasEstadia(dto.getDiasEstadia());
        parada.setCostoTotalEstadia(dto.getCostoTotalEstadia());
        parada.setOrdenEnRuta(dto.getOrdenEnRuta());
        parada.setRuta(rutaService.getRutaById(dto.getRutaId())); // Esto lo seteo en el RutaService cuando armo la ruta completa
        parada.setDepositoId(dto.getDepositoId());
        return parada;
    }
    //FIN Mappers

    @Transactional
    public ParadaEnDeposito save(ParadaEnDeposito parada) {
        return paradaRepo.save(parada);
    }

    public ParadaEnDeposito getParadaById(Integer paradaId) {
        return paradaRepo.findById(paradaId)
                .orElseThrow(() -> new RuntimeException("Parada en depósito no encontrada con ID: " + paradaId));
    }

    public ParadaEnDepositoDTO registrarLlegada(Integer paradaId) {
        ParadaEnDeposito parada = getParadaById(paradaId);
        parada.setFechaHoraLlegada(java.time.LocalDateTime.now());
        ParadaEnDeposito paradaActualizada = save(parada);
        return toDto(paradaActualizada);
    }
}
