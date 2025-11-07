package ar.edu.utn.frc.bda.k7.rutas.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frc.bda.k7.rutas.dtos.UbicacionDTO;
import ar.edu.utn.frc.bda.k7.rutas.entities.Ubicacion;
import ar.edu.utn.frc.bda.k7.rutas.services.UbicacionService;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@AllArgsConstructor
@RequestMapping("/api/ruta/ubicaciones")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    @GetMapping
    public List<UbicacionDTO> getUbicaciones() {
        return ubicacionService.getAllUbicaciones();
    }

    @PostMapping
    public ResponseEntity<Ubicacion> postUbicacion(@RequestBody UbicacionDTO dto) {
        if (dto.getDireccionTextual() == null &&
            (dto.getLatitud() == null ||
            dto.getLongitud() == null)) {
                return ResponseEntity.badRequest().build();
        }
        if (ubicacionService.getUbicacionById(dto.getId()) != null) {
            return ResponseEntity.status(409).build();
        }
        if (dto.getDireccionTextual() == null) {
            //aca buscar la direccion textual con el maps
        }
        if (dto.getLatitud() == null || dto.getLongitud() == null) {
            //aca buscar latitud y longitud con el maps
        }
        Ubicacion savedEntity = ubicacionService.saveUbicacion(dto);
        return ResponseEntity.status(201).body(savedEntity);
    }
    
}
