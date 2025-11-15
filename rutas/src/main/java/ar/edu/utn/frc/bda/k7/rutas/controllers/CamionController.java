package ar.edu.utn.frc.bda.k7.rutas.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frc.bda.k7.rutas.dtos.CamionDTO;
import ar.edu.utn.frc.bda.k7.rutas.services.CamionService;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@AllArgsConstructor
@RequestMapping("/api/ruta/camiones")
public class CamionController {

    private final CamionService camionService;

    @GetMapping
    public List<CamionDTO> getCamiones() {
        return camionService.getAllCamiones();
    }

    @PostMapping
    public ResponseEntity<CamionDTO> postCamion(@RequestBody CamionDTO dto) {
        
        if (dto.getDominio() == null || dto.getTransportistaLegajo() == null ||
            dto.getCapacidadPeso() == null || dto.getCapacidadVolumen() == null ||
            dto.getConsumoCombustiblePromedio() == null) {
                return ResponseEntity.badRequest().build();
        }

        if (camionService.getCamionByDominio(dto.getDominio()) != null) {
            return ResponseEntity.status(409).build();
        }

        camionService.saveCamion(dto);

        return ResponseEntity.status(201).body(dto);
    }

    @GetMapping("/{dominio}")
    public ResponseEntity<CamionDTO> getCamion(@PathVariable String dominio) {
        CamionDTO dto = camionService.getCamionByDominio(dominio);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/{dominio}")
    public ResponseEntity<CamionDTO> putCamion(@PathVariable String dominio, @RequestBody CamionDTO dto) {
        CamionDTO existingDto = camionService.getCamionByDominio(dominio);
        if (existingDto == null) {
            return ResponseEntity.notFound().build();
        }
        camionService.saveCamion(dto);
        return ResponseEntity.status(201).body(dto);
    }

    @DeleteMapping("/{dominio}")
    public ResponseEntity<Void> deleteCamion(@PathVariable String dominio) {
        CamionDTO existingDto = camionService.getCamionByDominio(dominio);
        if (existingDto == null) {
            return ResponseEntity.notFound().build();
        }
        camionService.deleteCamion(existingDto);
        return ResponseEntity.noContent().build();
    }
}
