package ar.edu.utn.frc.bda.k7.users.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import ar.edu.utn.frc.bda.k7.users.dto.TransportistaDTO;
import ar.edu.utn.frc.bda.k7.users.dto.DTOSintegracion.CreateTransportistaRequestDTO;
import ar.edu.utn.frc.bda.k7.users.service.TransportistaService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users/transportistas")
public class TransportistaController {
    
    private TransportistaService transportistaService;

    @GetMapping("/{legajo}")
    public ResponseEntity<TransportistaDTO> obtenerPorLegajo(@PathVariable String legajo) {
        TransportistaDTO dto = transportistaService.buscarPorLegajo(legajo);
        if (dto == null) {
        return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<TransportistaDTO>> obtenerTodosLosTransportistas(){
        return ResponseEntity.ok(transportistaService.obtenerTodos());
    }

    // @PostMapping
    // public ResponseEntity<TransportistaDTO> crearTransportista(@RequestBody TransportistaDTO transportistaDTO){
    //     transportistaService.crearTransportista(transportistaDTO);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(transportistaDTO);
    // }

    @PutMapping("/{legajo}")
    public ResponseEntity<?> actualizarTransportista(@PathVariable String legajo, @RequestBody TransportistaDTO transportistaDTO){
        try {
            TransportistaDTO actualizado = transportistaService.actualizar(legajo, transportistaDTO);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el transportista");
        }
    }

       @PostMapping
    public ResponseEntity<TransportistaDTO> crearTransportista(@RequestBody CreateTransportistaRequestDTO requestDTO) {
        
        try {
            TransportistaDTO transportistaCreado = transportistaService.crearTransportista(requestDTO);

            // Retorna 201 Created si es exitoso
            return ResponseEntity.status(HttpStatus.CREATED).body(transportistaCreado);
            
        } catch (ResourceAccessException e) {
            // CAPTURA LA FALLA DE CONEXIÓN A INT-KEYCLOAK
            
            // Loguear el error para diagnóstico (opcional, el servicio ya lo hace)
            System.err.println("Error al conectar con el servicio Keycloak: " + e.getMessage());

            // Devuelve 503 Service Unavailable (dependencia interna no disponible)
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(null); 
            
        } catch (RuntimeException e) {
            // Capturar otras RuntimeException (ej. validación, usuario duplicado)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
