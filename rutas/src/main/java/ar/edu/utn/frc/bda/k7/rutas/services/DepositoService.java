package ar.edu.utn.frc.bda.k7.rutas.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.utn.frc.bda.k7.rutas.dtos.DepositoDTO;
import ar.edu.utn.frc.bda.k7.rutas.dtos.UbicacionDTO;
import ar.edu.utn.frc.bda.k7.rutas.entities.Deposito;
import ar.edu.utn.frc.bda.k7.rutas.entities.Ubicacion;
import ar.edu.utn.frc.bda.k7.rutas.repositories.DepositoRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DepositoService {

    private final DepositoRepo depositoRepo;
    private final UbicacionService ubicacionService;

    //Mappers
    public DepositoDTO toDTO(Deposito entity) {
        if (entity == null) return null;
        
        DepositoDTO dto = new DepositoDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setCostoEstadiaDiario(entity.getCostoEstadiaDiario());
        dto.setUbicacion(ubicacionService.toDTO(entity.getUbicacion()));
        return dto;
    }

    public Deposito toDeposito(DepositoDTO dto) {
        Deposito entity = new Deposito();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setCostoEstadiaDiario(dto.getCostoEstadiaDiario());
        
        // NO asignar la ubicación aquí, se hace en saveDeposito
        
        return entity;
    }
    //FIN Mappers

    public ArrayList<DepositoDTO> getAllDepositos() {
        ArrayList<DepositoDTO> depositosDTO = new ArrayList<>();
        for (Deposito deposito : depositoRepo.findAll()) {
            depositosDTO.add(toDTO(deposito));
        }
        return depositosDTO;
    }

    @Transactional
    public DepositoDTO saveDeposito(DepositoDTO dto) {
        // 1. Guardar la ubicación (que internamente buscará la ciudad por ID)
        UbicacionDTO ubicacionGuardada = ubicacionService.saveUbicacion(dto.getUbicacion());
        
        // 2. Crear el depósito
        Deposito deposito = new Deposito();
        deposito.setNombre(dto.getNombre());
        deposito.setCostoEstadiaDiario(dto.getCostoEstadiaDiario());
        deposito.setUbicacion(ubicacionService.toUbicacion(ubicacionGuardada));
        
        // 3. Guardar el depósito
        Deposito depositoGuardado = depositoRepo.save(deposito);
        
        return toDTO(depositoGuardado);
    }

    public DepositoDTO getDepositoById(Integer depositoId) {
        return toDTO(depositoRepo.findById(depositoId).orElse(null));
    }
    
    public DepositoDTO getDepositoByUbicacionId(Integer ubicacionId) {
        Deposito deposito = depositoRepo.findByUbicacionId(ubicacionId);
        return deposito != null ? toDTO(deposito) : null;
    }

}