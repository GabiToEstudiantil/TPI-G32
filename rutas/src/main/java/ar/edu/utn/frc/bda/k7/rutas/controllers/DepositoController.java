package ar.edu.utn.frc.bda.k7.rutas.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.utn.frc.bda.k7.rutas.dtos.DepositoDTO;
import ar.edu.utn.frc.bda.k7.rutas.entities.Deposito;
import ar.edu.utn.frc.bda.k7.rutas.services.DepositoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@AllArgsConstructor
@RequestMapping("/api/ruta/depositos")
public class DepositoController {
    
    private final DepositoService depositoService;

    @GetMapping
    public ResponseEntity<List<DepositoDTO>> getDepositos() {
        List<DepositoDTO> depositos = depositoService.getAllDepositos();
        return ResponseEntity.ok(depositos);
    }

    @PostMapping
    public ResponseEntity<DepositoDTO> postDeposito(@RequestBody DepositoDTO dto) {
        if (dto.getNombre() == null || dto.getCostoEstadiaDiario() == null ||
            (dto.getUbicacion().getDireccionTextual() == null &&
            dto.getUbicacion().getLatitud() == null && dto.getUbicacion().getLongitud() == null)) {
                return ResponseEntity.badRequest().build();
        }
        if (depositoService.getDepositoById(dto.getId()) != null) {
            return ResponseEntity.status(409).build();
        }
        DepositoDTO entity = depositoService.saveDeposito(dto);
        return ResponseEntity.status(201).body(entity);
    }

    @PutMapping("/{depositoId}")
    public ResponseEntity<DepositoDTO> putDeposito(@PathVariable String depositoId, @RequestBody DepositoDTO dto) {
        if (dto.getNombre() == null || dto.getCostoEstadiaDiario() == null ||
            (dto.getUbicacion().getDireccionTextual() == null &&
            dto.getUbicacion().getLatitud() == null && dto.getUbicacion().getLongitud() == null)) {
                return ResponseEntity.badRequest().build();
        }

        if (depositoService.getDepositoById(Integer.parseInt(depositoId)) == null) {
            return ResponseEntity.notFound().build();
        }
        
        DepositoDTO entity = depositoService.saveDeposito(dto);
        return ResponseEntity.status(201).body(entity);
    }
    
}
