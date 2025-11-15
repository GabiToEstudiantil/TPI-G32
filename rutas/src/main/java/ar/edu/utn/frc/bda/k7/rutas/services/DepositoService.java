package ar.edu.utn.frc.bda.k7.rutas.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.utn.frc.bda.k7.rutas.dtos.DepositoDTO;
import ar.edu.utn.frc.bda.k7.rutas.entities.Deposito;
import ar.edu.utn.frc.bda.k7.rutas.repositories.DepositoRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DepositoService {

    private final DepositoRepo depositoRepo;
    private final UbicacionService ubicacionService;

    //Mappers
    public DepositoDTO toDTO(Deposito entity) {
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
        entity.setUbicacion(ubicacionService.toUbicacion(dto.getUbicacion()));
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
        Deposito deposito = toDeposito(dto);
        Deposito depositoGuardado = depositoRepo.save(deposito);
        return toDTO(depositoGuardado);
    }

    public DepositoDTO getDepositoById(Integer depositoId) {
        return toDTO(depositoRepo.findById(depositoId).orElse(null));
    }

}
