package ar.edu.utn.frc.bda.k7.rutas.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frc.bda.k7.rutas.dtos.TarifaVolumenDTO;
import ar.edu.utn.frc.bda.k7.rutas.entities.TarifaVolumen;
import ar.edu.utn.frc.bda.k7.rutas.services.TarifaVolumenService;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@AllArgsConstructor
@RequestMapping("/api/ruta/tarifas/volumen")
public class TarifaVolumenController {
    private final TarifaVolumenService tarifaVolumenService;

    @GetMapping
    public ResponseEntity<List<TarifaVolumenDTO>> getTarifasVolumen() {
        return ResponseEntity.ok(tarifaVolumenService.getAllTarifasVolumen());
    }

    @PostMapping
    public ResponseEntity<TarifaVolumenDTO> postTarifaVolumen(@RequestBody TarifaVolumenDTO dto) {
        if (dto.getVolumenMin() == null || dto.getVolumenMax() == null ||
            dto.getCostoKmBase() == null) {
                return ResponseEntity.badRequest().build();
        }
        //Luego de mucha quemada pensando como validar esto se me ocurrio el sig quilombo
        ArrayList<TarifaVolumenDTO> existingTarifas = tarifaVolumenService.getAllTarifasVolumen();

        boolean seCruza = existingTarifas.stream().anyMatch(t ->
            // Volumen minimo dentro del rango de otra tarifa
            (dto.getVolumenMin().compareTo(t.getVolumenMin()) >= 0 &&
            dto.getVolumenMin().compareTo(t.getVolumenMax()) <= 0)
            ||
            // Volumen maximo dentro del rango de otra tarifa
            (dto.getVolumenMax().compareTo(t.getVolumenMin()) >= 0 &&
            dto.getVolumenMax().compareTo(t.getVolumenMax()) <= 0)
            ||
            // si se llega a cubrir completamente el rango con otra tarifa
            (dto.getVolumenMin().compareTo(t.getVolumenMin()) <= 0 &&
            dto.getVolumenMax().compareTo(t.getVolumenMax()) >= 0)
        );
        if (seCruza) {
            return ResponseEntity.status(409).build();
        }
        //Fin de quilombo
        TarifaVolumenDTO entity = tarifaVolumenService.saveTarifaVolumen(dto);
        return ResponseEntity.status(201).body(entity);
    }

    @PutMapping("/{tarifaId}")
    public ResponseEntity<TarifaVolumenDTO> putTarifaVolumen(@PathVariable String tarifaId, @RequestBody TarifaVolumenDTO dto) {
        if (dto.getVolumenMin() == null || dto.getVolumenMax() == null ||
            dto.getCostoKmBase() == null) {
                return ResponseEntity.badRequest().build();
        }

        Integer id = Integer.parseInt(tarifaId);
        
        if (tarifaVolumenService.findTarifaById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        
        // ✅ ASIGNAR EL ID AL DTO ANTES DE VALIDAR
        dto.setId(id);
        
        ArrayList<TarifaVolumenDTO> existingTarifas = tarifaVolumenService.getAllTarifasVolumen();

        boolean seCruza = existingTarifas.stream().anyMatch(t ->
            // Solo validar si NO es la misma tarifa que estamos actualizando
            !t.getId().equals(id) && (
                // Volumen mínimo dentro del rango de otra tarifa
                (dto.getVolumenMin().compareTo(t.getVolumenMin()) >= 0 &&
                dto.getVolumenMin().compareTo(t.getVolumenMax()) <= 0)
                ||
                // Volumen máximo dentro del rango de otra tarifa
                (dto.getVolumenMax().compareTo(t.getVolumenMin()) >= 0 &&
                dto.getVolumenMax().compareTo(t.getVolumenMax()) <= 0)
                ||
                // Si se llega a cubrir completamente el rango con otra tarifa
                (dto.getVolumenMin().compareTo(t.getVolumenMin()) <= 0 &&
                dto.getVolumenMax().compareTo(t.getVolumenMax()) >= 0)
            )
        );
        
        if (seCruza) {
            return ResponseEntity.status(409).build();
        }
        
        TarifaVolumenDTO entity = tarifaVolumenService.saveTarifaVolumen(dto);
        return ResponseEntity.ok(entity); // ✅ 200 OK en lugar de 201 para PUT
    }
    
    
}
