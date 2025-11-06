package ar.edu.utn.frc.bda.k7.rutas.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.utn.frc.bda.k7.rutas.dtos.CamionDTO;
import ar.edu.utn.frc.bda.k7.rutas.entities.Camion;
import ar.edu.utn.frc.bda.k7.rutas.repositories.CamionRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CamionService {

    private final CamionRepo camionRepo;

    //Mappers
    public CamionDTO toDto(Camion camion) {
        CamionDTO camionDTO = new CamionDTO();
        camionDTO.setDominio(camion.getDominio());
        camionDTO.setTransportistaLegajo(camion.getTransportistaLegajo());
        camionDTO.setCapacidadPeso(camion.getCapacidadPeso());
        camionDTO.setCapacidadVolumen(camion.getCapacidadVolumen());
        camionDTO.setDisponible(camion.isDisponible());
        camionDTO.setConsumoCombustiblePromedio(camion.getConsumoCombustiblePromedio());
        return camionDTO;
    }

    public Camion toCamion(CamionDTO dto) {
        Camion camion = new Camion();
        camion.setDominio(dto.getDominio());
        camion.setTransportistaLegajo(dto.getTransportistaLegajo());
        camion.setCapacidadPeso(dto.getCapacidadPeso());
        camion.setCapacidadVolumen(dto.getCapacidadVolumen());
        camion.setDisponible(dto.isDisponible());
        camion.setConsumoCombustiblePromedio(dto.getConsumoCombustiblePromedio());
        return camion;
    }
    //FIN Mappers

    public ArrayList<CamionDTO> getAllCamiones() {
        ArrayList<CamionDTO> camionesDTO = new ArrayList<>();
        for (Camion camion : camionRepo.findAll()) {
            camionesDTO.add(toDto(camion));
        }
        return camionesDTO;
    }

    @Transactional
    public Camion saveCamion(CamionDTO dto) {
        return camionRepo.save(toCamion(dto));
    }

    public CamionDTO getCamionByDominio(String dominio) {
        return toDto(camionRepo.findById(dominio).orElse(null));
    }

    @Transactional
    public void deleteCamion(CamionDTO dto) {
        camionRepo.delete(toCamion(dto));
    }

}
