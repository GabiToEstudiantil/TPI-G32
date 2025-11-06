package ar.edu.utn.frc.bda.k7.rutas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.utn.frc.bda.k7.rutas.dtos.CiudadDTO;
import ar.edu.utn.frc.bda.k7.rutas.entities.Ciudad;
import ar.edu.utn.frc.bda.k7.rutas.repositories.CiudadRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CiudadService {
    
    private final CiudadRepo ciudadRepo;

    //Mappers
    public CiudadDTO toDTO(Ciudad entity) {
        CiudadDTO dto = new CiudadDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        return dto;
    }

    public Ciudad toCiudad(CiudadDTO dto) {
        Ciudad entity = new Ciudad();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        return entity;
    }
    //FIN Mappers
}
