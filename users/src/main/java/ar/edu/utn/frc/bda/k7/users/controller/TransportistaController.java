package ar.edu.utn.frc.bda.k7.users.controller;

import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.utn.frc.bda.k7.users.dto.TransportistaDTO;
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

    @PostMapping
    public ResponseEntity<TransportistaDTO> crearTransportista(@RequestBody TransportistaDTO transportistaDTO){
        transportistaService.crearTransportista(transportistaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transportistaDTO);
    }

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
}
