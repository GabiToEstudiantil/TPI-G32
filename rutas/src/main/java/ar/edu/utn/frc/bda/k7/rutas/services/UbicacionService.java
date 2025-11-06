package ar.edu.utn.frc.bda.k7.rutas.services;

import org.springframework.stereotype.Service;

import ar.edu.utn.frc.bda.k7.rutas.dtos.UbicacionDTO;
import ar.edu.utn.frc.bda.k7.rutas.entities.Ubicacion;
import ar.edu.utn.frc.bda.k7.rutas.repositories.UbicacionRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UbicacionService {
    
    private final UbicacionRepo ubicacionRepo;
    private final CiudadService ciudadService;

    //Mappers
    public UbicacionDTO toDTO(Ubicacion entity) {
        UbicacionDTO dto = new UbicacionDTO();
        dto.setId(entity.getId());
        dto.setLatitud(entity.getLatitud());
        dto.setLongitud(entity.getLongitud());
        dto.setDireccionTextual(entity.getDireccionTextual());
        dto.setCiudad(ciudadService.toDTO(entity.getCiudad()));
        return dto;
    }

    public Ubicacion toUbicacion(UbicacionDTO dto) {
        Ubicacion entity = new Ubicacion();
        entity.setId(dto.getId());
        entity.setLatitud(dto.getLatitud());
        entity.setLongitud(dto.getLongitud());
        entity.setDireccionTextual(dto.getDireccionTextual());
        entity.setCiudad(ciudadService.toCiudad(dto.getCiudad()));
        return entity;
    }
    //FIN Mappers


}
