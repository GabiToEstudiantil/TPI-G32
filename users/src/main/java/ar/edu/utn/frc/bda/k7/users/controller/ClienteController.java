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
import ar.edu.utn.frc.bda.k7.users.dto.ClienteDTO;
import ar.edu.utn.frc.bda.k7.users.service.ClienteService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users/clientes")
public class ClienteController {
    
    private ClienteService clienteService;

    @GetMapping("/{dni}")
    public ResponseEntity<ClienteDTO> obtenerPorDni(@PathVariable Integer dni) {
       ClienteDTO dto = clienteService.buscarPorDni(dni);
       if (dto == null) {
        return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(dto);
    }

    @GetMapping
    public List<ClienteDTO> obtenerTodosLosClientes(){
        return clienteService.obtenerTodos();
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> crearCliente(@RequestBody ClienteDTO clienteDTO) {
        clienteService.crearCliente(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteDTO);
    }


    @PutMapping("/{dni}")
        public ResponseEntity<?> actualizarCliente(@PathVariable Integer dni, @RequestBody ClienteDTO clienteDTO) {
        try {
            ClienteDTO actualizado = clienteService.actualizar(dni, clienteDTO);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el cliente");
        }
    }
}

