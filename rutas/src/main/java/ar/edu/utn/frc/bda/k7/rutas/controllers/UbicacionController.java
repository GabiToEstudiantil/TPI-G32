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
        // necesitamos una dirección O coordenadas
        if (dto.getDireccionTextual() == null &&
            (dto.getLatitud() == null || dto.getLongitud() == null)) {
                return ResponseEntity.badRequest().build();
        }

        Ubicacion savedEntity = ubicacionService.saveUbicacion(dto);
        
        return ResponseEntity.status(201).body(savedEntity);
    }
    
}
