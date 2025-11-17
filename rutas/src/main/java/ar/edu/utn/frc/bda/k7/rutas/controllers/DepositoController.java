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
    public ResponseEntity<?> postDeposito(@RequestBody DepositoDTO dto) {
        try {
            // Validaciones básicas
            if (dto.getNombre() == null || dto.getCostoEstadiaDiario() == null) {
                return ResponseEntity.badRequest().body("Nombre y costo son obligatorios");
            }
            
            if (dto.getUbicacion() == null) {
                return ResponseEntity.badRequest().body("Ubicación es obligatoria");
            }
            
            if (dto.getUbicacion().getCiudad() == null || dto.getUbicacion().getCiudad().getId() == null) {
                return ResponseEntity.badRequest().body("Ciudad es obligatoria");
            }
            
            // Validar que tenga al menos dirección O coordenadas
            if (dto.getUbicacion().getDireccionTextual() == null &&
                (dto.getUbicacion().getLatitud() == null || dto.getUbicacion().getLongitud() == null)) {
                return ResponseEntity.badRequest().body("Debe proporcionar dirección o coordenadas");
            }
            
            // NO validar si existe (en POST siempre es nuevo)
            // El ID se genera automáticamente
            
            DepositoDTO entity = depositoService.saveDeposito(dto);
            return ResponseEntity.status(201).body(entity);
            
        } catch (Exception e) {
            System.err.println("Error al crear depósito: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
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
