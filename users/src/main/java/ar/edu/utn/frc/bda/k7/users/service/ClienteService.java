package ar.edu.utn.frc.bda.k7.users.service;

import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ar.edu.utn.frc.bda.k7.users.repository.ClientRepository;
import lombok.AllArgsConstructor;
import ar.edu.utn.frc.bda.k7.users.clients.KeycloakServiceClient;
import ar.edu.utn.frc.bda.k7.users.dto.DTOSintegracion.ClienteCreacionRequestDTO;
import ar.edu.utn.frc.bda.k7.users.dto.DTOSintegracion.CreateUserRequestDTO;
import ar.edu.utn.frc.bda.k7.users.domain.Cliente;
import ar.edu.utn.frc.bda.k7.users.dto.ClienteDTO;

@Service
@AllArgsConstructor
public class ClienteService {

    private final ClientRepository clienteRepository;
    private final KeycloakServiceClient keycloakServiceClient;

    public ClienteDTO toDto(Cliente cliente) {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setDni(cliente.getDni());
        clienteDTO.setKeycloak_id(cliente.getKeycloak_id());
        clienteDTO.setApellido(cliente.getApellido());
        clienteDTO.setEmail(cliente.getEmail());
        clienteDTO.setTelefono(cliente.getTelefono());
        clienteDTO.setNombre(cliente.getNombre());
        return clienteDTO;
    }

    public Cliente toCliente(ClienteCreacionRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setDni(dto.getDni());
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());
        return cliente;
    }
    
    public Cliente toCliente(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setKeycloak_id(dto.getKeycloak_id());
        cliente.setDni(dto.getDni());
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());
        return cliente;
    }
    
    public ArrayList<ClienteDTO> obtenerTodos(){
        ArrayList<ClienteDTO> clienteDTO = new ArrayList<>();
        for (Cliente cliente : clienteRepository.findAll()){
            clienteDTO.add(toDto(cliente));
        }
        return clienteDTO;
    }

    public ClienteDTO buscarPorDni(String dni){
        Cliente cliente = clienteRepository.findById(dni).orElse(null);
        return cliente != null ? toDto(cliente) : null;
    }

    @Transactional
    public ClienteDTO actualizar(String dni, ClienteDTO dto) {
        Cliente existente = clienteRepository.findById(dni)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con DNI: " + dni));

        existente.setNombre(dto.getNombre());
        existente.setApellido(dto.getApellido());
        existente.setEmail(dto.getEmail());
        existente.setTelefono(dto.getTelefono());
        existente.setKeycloak_id(dto.getKeycloak_id());

        Cliente actualizado = clienteRepository.save(existente);
        return toDto(actualizado);
    }
    

    @Transactional
    public ClienteDTO crearCliente(ClienteCreacionRequestDTO creationDto){
        
        // 1. Preparar el DTO para el microservicio Keycloak
        CreateUserRequestDTO keycloakRequest = createKeycloakRequest(creationDto);

        // 2. LLAMADA AL MICROSERVICIO KEYCLOAK. Obtiene el ID generado (UUID).
        String keycloakId = keycloakServiceClient.crearUsuario(keycloakRequest);

        // 3. Mapear DTO de Request a la Entidad Cliente
        Cliente cliente = toCliente(creationDto);
        
        // 4. Asignar el ID DE KEYCLOAK (UUID) a la Entidad local
        cliente.setKeycloak_id(keycloakId);

        // 5. Guardar la entidad en la base de datos local
        Cliente clienteGuardado = clienteRepository.save(cliente);

        // 6. Retornar el DTO de Respuesta
        return toDto(clienteGuardado);
    }
    
    private CreateUserRequestDTO createKeycloakRequest(ClienteCreacionRequestDTO dto) {
        CreateUserRequestDTO request = new CreateUserRequestDTO();
        
        request.setUsername(dto.getDni());
        request.setEmail(dto.getEmail());
        request.setPassword(dto.getPassword());
        request.setRole("ROLE_CLIENTE");
        
        return request;
    }
}
