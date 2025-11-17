package ar.edu.frc.utn.bda.k7.solicitudes.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.frc.utn.bda.k7.solicitudes.domain.Contenedor;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.ContenedorDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.services.ContenedorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/contenedores")
@AllArgsConstructor
public class ContenedorController {
    
    private final ContenedorService contenedorService;

    @GetMapping("/cliente/{dni}")
    public ResponseEntity<List<ContenedorDTO>> obtenerContenedoresPorDniCliente(@PathVariable String dni) {
        List<ContenedorDTO> contenedores = contenedorService.obtenerPorDniCliente(dni);
        return ResponseEntity.ok(contenedores);
    }

    @PostMapping
    public ResponseEntity<ContenedorDTO> crearContenedor(@RequestBody ContenedorDTO entity) {
        Contenedor contenedor = contenedorService.toContenedor(entity);
        Contenedor savedContenedor = contenedorService.save(contenedor);
        ContenedorDTO savedDto = contenedorService.toDto(savedContenedor);
        return ResponseEntity.status(201).body(savedDto);
    }

    @GetMapping("/{contenedorId}")
    public ResponseEntity<ContenedorDTO> obtenerContenedorPorId(@PathVariable Integer contenedorId) {
        Contenedor contenedor = contenedorService.obtenerPorId(contenedorId);
        if (contenedor == null) {
            return ResponseEntity.notFound().build();
        }
        ContenedorDTO contenedorDTO = contenedorService.toDto(contenedor);
        return ResponseEntity.ok(contenedorDTO);
    }
    
    @PutMapping("/{contenedorId}")
    public ResponseEntity<ContenedorDTO> actualizarContenedor(@PathVariable Integer contenedorId, @RequestBody ContenedorDTO dto) {
        Contenedor contenedor = contenedorService.obtenerPorId(contenedorId);
        if (contenedor == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Actualizar campos
        contenedor.setCodigoIdentificacion(dto.getCodigoIdentificacion());
        contenedor.setPeso(dto.getPeso());
        contenedor.setVolumen(dto.getVolumen());
        contenedor.setEstado(dto.getEstado());
        contenedor.setClienteDni(dto.getClienteDni());
        
        Contenedor savedContenedor = contenedorService.save(contenedor);
        ContenedorDTO savedDto = contenedorService.toDto(savedContenedor);
        return ResponseEntity.ok(savedDto);
    }
}