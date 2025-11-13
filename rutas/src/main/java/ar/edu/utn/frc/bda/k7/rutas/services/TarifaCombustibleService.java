package ar.edu.utn.frc.bda.k7.rutas.services;

import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ar.edu.utn.frc.bda.k7.rutas.dtos.TarifaCombustibleDTO;
import ar.edu.utn.frc.bda.k7.rutas.entities.TarifaCombustible;
import ar.edu.utn.frc.bda.k7.rutas.repositories.TarifaCombustibleRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TarifaCombustibleService {

    private final TarifaCombustibleRepo tarifaCombustibleRepo;

    //Mappers
    public TarifaCombustibleDTO toDTO(TarifaCombustible entity) {
        TarifaCombustibleDTO dto = new TarifaCombustibleDTO();
        dto.setId(entity.getId());
        dto.setPrecioLitro(entity.getPrecioLitro());
        dto.setNombre(entity.getNombre());
        return dto;
    }

    public TarifaCombustible toEntity(TarifaCombustibleDTO dto) {
        TarifaCombustible entity = new TarifaCombustible();
        entity.setId(dto.getId());
        entity.setPrecioLitro(dto.getPrecioLitro());
        entity.setNombre(dto.getNombre());
        return entity;
    }
    //FIN Mappers

    public ArrayList<TarifaCombustibleDTO> getAllTarifas() {
        ArrayList<TarifaCombustibleDTO> tarifasDTO = new ArrayList<>();
        for (TarifaCombustible tarifa : tarifaCombustibleRepo.findAll()) {
            tarifasDTO.add(toDTO(tarifa));
        }
        return tarifasDTO;
    }

    @Transactional
    public TarifaCombustible saveTarifaCombustible(TarifaCombustibleDTO dto) {
        TarifaCombustible entity = toEntity(dto);
        return tarifaCombustibleRepo.save(entity);
    }
    
    public TarifaCombustible findTarifaById(Integer tarifaId) {
        return tarifaCombustibleRepo.findById(tarifaId).orElse(null);
    }
}
