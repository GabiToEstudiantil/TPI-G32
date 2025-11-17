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
import ar.edu.utn.frc.bda.k7.users.dto.ClienteDTO;
import ar.edu.utn.frc.bda.k7.users.dto.DTOSintegracion.ClienteCreacionRequestDTO;
import ar.edu.utn.frc.bda.k7.users.service.ClienteService;
import org.springframework.web.client.ResourceAccessException;

@RestController
@RequestMapping("/api/users/clientes")
public class ClienteController {
    
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService){
        this.clienteService = clienteService;
    }

    @GetMapping("/{dni}")
    public ResponseEntity<ClienteDTO> obtenerPorDni(@PathVariable String dni) {
       ClienteDTO dto = clienteService.buscarPorDni(dni);
       if (dto == null) {
        return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> obtenerTodosLosClientes(){
        return ResponseEntity.ok(clienteService.obtenerTodos());
    }

    // @PostMapping
    // public ResponseEntity<ClienteDTO> crearCliente(@RequestBody ClienteDTO clienteDTO) {
    //     clienteService.crearCliente(clienteDTO);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(clienteDTO);
    // }


    @PutMapping("/{dni}")
        public ResponseEntity<?> actualizarCliente(@PathVariable String dni, @RequestBody ClienteDTO clienteDTO) {
        try {
            ClienteDTO actualizado = clienteService.actualizar(dni, clienteDTO);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el cliente");
        }
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> crearCliente(@RequestBody ClienteCreacionRequestDTO requestDTO) {
        
        try {
            ClienteDTO clienteCreado = clienteService.crearCliente(requestDTO);

            // Retorna 201 Created si es exitoso
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteCreado);
            
        } catch (ResourceAccessException e) {
            
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
