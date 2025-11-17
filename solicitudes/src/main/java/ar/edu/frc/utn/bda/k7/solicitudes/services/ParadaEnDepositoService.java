package ar.edu.frc.utn.bda.k7.solicitudes.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.frc.utn.bda.k7.solicitudes.domain.ParadaEnDeposito;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Ruta;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.ParadaEnDepositoDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.repositories.ParadaEnDepositoRepo;
import ar.edu.frc.utn.bda.k7.solicitudes.repositories.RutaRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParadaEnDepositoService {

    private final RutaRepo rutaRepo;
    
    private final ParadaEnDepositoRepo paradaRepo;

    //Mappers
    public ParadaEnDepositoDTO toDto(ParadaEnDeposito parada) {
        ParadaEnDepositoDTO dto = new ParadaEnDepositoDTO();
        dto.setId(parada.getId());
        dto.setFechaHoraLlegada(parada.getFechaHoraLlegada());
        dto.setFechaHoraSalida(parada.getFechaHoraSalida());
        dto.setSegundosEstadia(parada.getSegundosEstadia());
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
        parada.setSegundosEstadia(dto.getSegundosEstadia());
        parada.setCostoTotalEstadia(dto.getCostoTotalEstadia());
        parada.setOrdenEnRuta(dto.getOrdenEnRuta());
        parada.setRuta(rutaRepo.findById(dto.getRutaId()).orElseThrow(()
            -> new RuntimeException("Ruta no encontrada con id: " + dto.getRutaId())));
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

    public ParadaEnDepositoDTO getParadaEnDepositoByRutaAndOrden (Integer rutaId, Integer ordenEnRuta) {
        ParadaEnDeposito parada = paradaRepo.findByRutaIdAndOrdenEnRuta(rutaId  , ordenEnRuta);
        return toDto(parada);
    }

    public ParadaEnDepositoDTO guardarTiempoEstadia(ParadaEnDepositoDTO paradaDto) {

        ParadaEnDeposito parada = toParada(paradaDto); // Pasamos a entidad
        parada.setFechaHoraSalida(LocalDateTime.now()); // Seteamos salida

        LocalDateTime fechaHoraLlegada = parada.getFechaHoraLlegada(); // Obtenemos llegada
        LocalDateTime fechaHoraSalida = parada.getFechaHoraSalida(); // Obtenemos salida

        Long segundosEstadia = ChronoUnit.SECONDS.between(fechaHoraLlegada, fechaHoraSalida); // Calculamos duracion de estadia
        parada.setSegundosEstadia(segundosEstadia); // Seteamos segundos de estadia
        ParadaEnDeposito paradaActualizada = save(parada);
        return toDto(paradaActualizada);
    }

    public List<ParadaEnDepositoDTO> getParadaEnDepositoByRuta(Ruta ruta) {
        List<ParadaEnDeposito> paradas = paradaRepo.findByRutaId(ruta.getId());
        return paradas.stream().map(this::toDto).toList();
    }
}
