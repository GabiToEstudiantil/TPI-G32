package ar.edu.utn.frc.bda.k7.rutas.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.GeoApiClient;
import ar.edu.utn.frc.bda.k7.rutas.dtos.UbicacionDTO;
import ar.edu.utn.frc.bda.k7.rutas.entities.Ubicacion;
import ar.edu.utn.frc.bda.k7.rutas.repositories.UbicacionRepo;
import jakarta.transaction.Transactional;
import ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.dtos.GeocodingDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UbicacionService {
    
    private final UbicacionRepo ubicacionRepo;
    private final CiudadService ciudadService;
    private final GeoApiClient geoApiClient;

    //Mappers
    public UbicacionDTO toDTO(Ubicacion entity) {
        UbicacionDTO dto = new UbicacionDTO();
        dto.setId(entity.getId());
        dto.setLatitud(entity.getLatitud());
        dto.setLongitud(entity.getLongitud());
        dto.setDireccionTextual(entity.getDireccionTextual());
        if (entity.getCiudad() != null) {
            dto.setCiudad(ciudadService.toDTO(entity.getCiudad()));
        }
        return dto;
    }

    public Ubicacion toUbicacion(UbicacionDTO dto) {
        Ubicacion entity = new Ubicacion();
        entity.setId(dto.getId());
        entity.setLatitud(dto.getLatitud());
        entity.setLongitud(dto.getLongitud());
        entity.setDireccionTextual(dto.getDireccionTextual());
        if (dto.getCiudad() != null) {
            entity.setCiudad(ciudadService.toCiudad(dto.getCiudad()));
        }
        return entity;
    }
    //FIN Mappers

    public ArrayList<UbicacionDTO> getAllUbicaciones() {
        ArrayList<UbicacionDTO> ubicaciones = new ArrayList<>();
        for (Ubicacion ubicacion : ubicacionRepo.findAll()) {
            ubicaciones.add(toDTO(ubicacion));
        }
        return ubicaciones;
    }

    public UbicacionDTO getUbicacionById(Integer ubicacionId) {
        Ubicacion entity = ubicacionRepo.findById(ubicacionId).orElse(null);
        return (entity != null) ? toDTO(entity) : null;
    }

    @Transactional
    public Ubicacion saveUbicacion(UbicacionDTO dto) {
        
        // Si no tenemos lat/lng pero sí dirección...
        if (dto.getDireccionTextual() != null && (dto.getLatitud() == null || dto.getLongitud() == null)) {
            GeocodingDTO geoInfo = geoApiClient.geocode(dto.getDireccionTextual());
            if (geoInfo != null) {
                dto.setLatitud(geoInfo.getLat());
                dto.setLongitud(geoInfo.getLng());
            }
        }
        // Si no tenemos dirección pero sí lat/lng...
        else if (dto.getDireccionTextual() == null && (dto.getLatitud() != null && dto.getLongitud() != null)) {
            GeocodingDTO geoInfo = geoApiClient.reverseGeocode(dto.getLatitud(), dto.getLongitud());
            if (geoInfo != null) {
                dto.setDireccionTextual(geoInfo.getFormattedAddress());
            }
        }

        Ubicacion entity = toUbicacion(dto);
        return ubicacionRepo.save(entity);
    }
}
