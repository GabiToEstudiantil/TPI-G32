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
    public List<TarifaVolumenDTO> getTarifasVolumen() {
        return tarifaVolumenService.getAllTarifasVolumen();
    }

    @PostMapping
    public ResponseEntity<TarifaVolumen> postTarifaVolumen(@RequestBody TarifaVolumenDTO dto) {
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
        TarifaVolumen entity = tarifaVolumenService.saveTarifaVolumen(dto);
        return ResponseEntity.status(201).body(entity);
    }

    @PutMapping("/{tarifaId}")
    public ResponseEntity<TarifaVolumen> putTarifaVolumen(@PathVariable String tarifaId, @RequestBody TarifaVolumenDTO dto) {
        if (dto.getVolumenMin() == null || dto.getVolumenMax() == null ||
            dto.getCostoKmBase() == null) {
                return ResponseEntity.badRequest().build();
        }

        if (tarifaVolumenService.findTarifaById(Integer.parseInt(tarifaId)) == null) {
            return ResponseEntity.notFound().build();
        }
        ArrayList<TarifaVolumenDTO> existingTarifas = tarifaVolumenService.getAllTarifasVolumen();

        boolean seCruza = existingTarifas.stream().anyMatch(t ->
            ((dto.getVolumenMin().compareTo(t.getVolumenMin()) >= 0 &&
            dto.getVolumenMin().compareTo(t.getVolumenMax()) <= 0)
            ||
            (dto.getVolumenMax().compareTo(t.getVolumenMin()) >= 0 &&
            dto.getVolumenMax().compareTo(t.getVolumenMax()) <= 0)
            ||
            (dto.getVolumenMin().compareTo(t.getVolumenMin()) <= 0 &&
            dto.getVolumenMax().compareTo(t.getVolumenMax()) >= 0)
            && !t.getId().equals(Integer.parseInt(tarifaId))) // Sin contar la tarifa que estamos actualizando
        );
        if (seCruza) {
            return ResponseEntity.status(409).build();
        }
        TarifaVolumen entity = tarifaVolumenService.saveTarifaVolumen(dto);
        return ResponseEntity.status(201).body(entity);
    }
    
    
}
