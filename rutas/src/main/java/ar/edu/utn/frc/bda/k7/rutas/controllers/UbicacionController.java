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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@AllArgsConstructor
@RequestMapping("/api/ruta/ubicaciones")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    @GetMapping
    public ResponseEntity<List<UbicacionDTO>> getUbicaciones() {
        return ResponseEntity.ok(ubicacionService.getAllUbicaciones());
    }

    @PostMapping
    public ResponseEntity<UbicacionDTO> postUbicacion(@RequestBody UbicacionDTO dto) {
        // necesitamos una dirección O coordenadas
        if (dto.getDireccionTextual() == null &&
            (dto.getLatitud() == null || dto.getLongitud() == null)) {
                return ResponseEntity.badRequest().build();
        }

        UbicacionDTO savedEntity = ubicacionService.saveUbicacion(dto);
        
        return ResponseEntity.status(201).body(savedEntity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDTO> getUbicacionById(@PathVariable Integer id) {
        UbicacionDTO dto = ubicacionService.getUbicacionById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
}
