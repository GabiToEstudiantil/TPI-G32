package ar.edu.utn.frc.bda.k7.rutas.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frc.bda.k7.rutas.dtos.TarifaCombustibleDTO;
import ar.edu.utn.frc.bda.k7.rutas.entities.TarifaCombustible;
import ar.edu.utn.frc.bda.k7.rutas.services.TarifaCombustibleService;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@AllArgsConstructor
@RequestMapping("/api/ruta/tarifa/combustible")
public class TarifaCombustibleController {
    
    private final TarifaCombustibleService tarifaCombustibleService;

    @GetMapping
    public List<TarifaCombustibleDTO> getTarifasCombustible() {
        return tarifaCombustibleService.getAllTarifas();
    }

    @PostMapping
    public ResponseEntity<TarifaCombustible> postTarifaCombustible(@RequestBody TarifaCombustibleDTO dto) {
        if (dto.getNombre() == null || dto.getPrecioLitro() == null) {
            return ResponseEntity.badRequest().build();
        }
        TarifaCombustible savedEntity = tarifaCombustibleService.saveTarifaCombustible(dto);
        return ResponseEntity.ok(savedEntity);
    }

    @PutMapping("/{tarifaId}")
    public ResponseEntity<TarifaCombustible> putTarifaCombustible(@PathVariable String tarifaId, @RequestBody TarifaCombustibleDTO dto) {
        if (dto.getNombre() == null || dto.getPrecioLitro() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (tarifaCombustibleService.findTarifaById(Integer.parseInt(tarifaId)) == null) {
            return ResponseEntity.notFound().build();
        }
        TarifaCombustible updatedEntity = tarifaCombustibleService.saveTarifaCombustible(dto);
        return ResponseEntity.ok(updatedEntity);
    }
    
}
