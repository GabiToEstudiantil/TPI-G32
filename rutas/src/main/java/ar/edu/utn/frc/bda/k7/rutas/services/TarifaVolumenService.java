package ar.edu.utn.frc.bda.k7.rutas.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.utn.frc.bda.k7.rutas.dtos.TarifaVolumenDTO;
import ar.edu.utn.frc.bda.k7.rutas.entities.TarifaVolumen;
import ar.edu.utn.frc.bda.k7.rutas.repositories.TarifaVolumenRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TarifaVolumenService {
    
    private final TarifaVolumenRepo tarifaVolumenRepo;

        //Mappers
    public TarifaVolumenDTO toDTO(TarifaVolumen entity) {
        TarifaVolumenDTO dto = new TarifaVolumenDTO();
        dto.setId(entity.getId());
        dto.setVolumenMin(entity.getVolumenMin());
        dto.setVolumenMax(entity.getVolumenMax());
        dto.setCostoKmBase(entity.getCostoKmBase());
        return dto;
    }

    public TarifaVolumen toEntity(TarifaVolumenDTO dto) {
        TarifaVolumen entity = new TarifaVolumen();
        entity.setId(dto.getId());
        entity.setVolumenMin(dto.getVolumenMin());
        entity.setVolumenMax(dto.getVolumenMax());
        entity.setCostoKmBase(dto.getCostoKmBase());
        return entity;
    }
    //FIN Mappers

    public ArrayList<TarifaVolumenDTO> getAllTarifasVolumen() {
        ArrayList<TarifaVolumenDTO> tarifasDTO = new ArrayList<>();
        for (TarifaVolumen tarifa : tarifaVolumenRepo.findAll()) {
            tarifasDTO.add(toDTO(tarifa));
        }
        return tarifasDTO;
    }

    @Transactional
    public TarifaVolumenDTO saveTarifaVolumen(TarifaVolumenDTO dto) {
        TarifaVolumen entity;
        
        // Si tiene ID, es un UPDATE - buscar la entidad existente
        if (dto.getId() != null) {
            entity = tarifaVolumenRepo.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada con ID: " + dto.getId()));
            
            // Actualizar solo los campos modificables
            entity.setVolumenMin(dto.getVolumenMin());
            entity.setVolumenMax(dto.getVolumenMax());
            entity.setCostoKmBase(dto.getCostoKmBase());
        } else {
            // Si no tiene ID, es un INSERT - crear nueva entidad
            entity = toEntity(dto);
        }
        
        TarifaVolumen savedEntity = tarifaVolumenRepo.save(entity);
        return toDTO(savedEntity);
    }

    public TarifaVolumen findTarifaById(Integer tarifaId) {
        return tarifaVolumenRepo.findById(tarifaId).orElse(null);
    }

    public TarifaVolumen findByVolumen(Double volumen) {
        return tarifaVolumenRepo.findByVolumen(volumen);
    }

}
