package ar.edu.utn.frc.bda.k7.users.service;


import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ar.edu.utn.frc.bda.k7.users.clients.KeycloakServiceClient;
import ar.edu.utn.frc.bda.k7.users.domain.Transportista;
import ar.edu.utn.frc.bda.k7.users.dto.TransportistaDTO;
import ar.edu.utn.frc.bda.k7.users.dto.DTOSintegracion.CreateTransportistaRequestDTO;
import ar.edu.utn.frc.bda.k7.users.dto.DTOSintegracion.CreateUserRequestDTO;
import ar.edu.utn.frc.bda.k7.users.repository.TransportistaRepository;

@Service
public class TransportistaService {
    
    private final TransportistaRepository transportistaRepository;
    private final KeycloakServiceClient keycloakServiceClient;

    public TransportistaService(TransportistaRepository transportistaRepository, KeycloakServiceClient keycloakServiceClient){
        this.transportistaRepository = transportistaRepository;
        this.keycloakServiceClient = keycloakServiceClient;
    }

    public TransportistaDTO toDto(Transportista transportista) {
        TransportistaDTO transportistaDTO = new TransportistaDTO();
        transportistaDTO.setDni(transportista.getDni());
        transportistaDTO.setLegajo(transportista.getLegajo());
        transportistaDTO.setApellido(transportista.getApellido());
        transportistaDTO.setEmail(transportista.getEmail());
        transportistaDTO.setTelefono(transportista.getTelefono());
        transportistaDTO.setNombre(transportista.getNombre());
        transportistaDTO.setKeycloak_id(transportista.getKeycloak_id());
        return transportistaDTO;
    }

    // public Transportista toTransportista(TransportistaDTO dto) {
    //     Transportista transportista = new Transportista();
    //     transportista.setLegajo(dto.getLegajo());
    //     transportista.setDni(dto.getDni());
    //     transportista.setNombre(dto.getNombre());
    //     transportista.setApellido(dto.getApellido());
    //     transportista.setTelefono(dto.getTelefono());
    //     transportista.setEmail(dto.getEmail());
    //     transportista.setKeycloak_id(dto.getKeycloak_id());
    //     return transportista;
    // }
    public Transportista toTransportista(CreateTransportistaRequestDTO dto) {
        Transportista transportista = new Transportista();
        transportista.setDni(dto.getDni());
        transportista.setNombre(dto.getNombre());
        transportista.setApellido(dto.getApellido());
        transportista.setTelefono(dto.getTelefono());
        transportista.setEmail(dto.getEmail());
        return transportista;
    }

    public ArrayList<TransportistaDTO> obtenerTodos(){
        ArrayList<TransportistaDTO> transportistaDTO = new ArrayList<>();
        for (Transportista transportista : transportistaRepository.findAll()){
            transportistaDTO.add(toDto(transportista));
        }
        return transportistaDTO;
    }

    public TransportistaDTO buscarPorLegajo(String legajo){
        return toDto(transportistaRepository.findById(legajo).orElse(null));
    }

    // @Transactional
    // public Transportista crearTransportista(TransportistaDTO dto){
    //     return transportistaRepository.save(toTransportista(dto));
    // }

    @Transactional
    public TransportistaDTO actualizar(String legajo, TransportistaDTO dto) {
        Transportista existente = transportistaRepository.findById(legajo)
            .orElseThrow(() -> new RuntimeException("Transportista no encontrado con legajo: " + legajo));
        
        existente.setNombre(dto.getNombre());
        existente.setApellido(dto.getApellido());
        existente.setEmail(dto.getEmail());
        existente.setTelefono(dto.getTelefono());
        existente.setKeycloak_id(dto.getKeycloak_id());

        Transportista actualizado = transportistaRepository.save(existente);
        return toDto(actualizado);
    }


    @Transactional
    public TransportistaDTO crearTransportista(CreateTransportistaRequestDTO creationDto){
        
        // 1. Preparar el DTO para el microservicio Keycloak
        CreateUserRequestDTO keycloakRequest = createKeycloakRequest(creationDto);

        // 2. LLAMADA AL MICROSERVICIO KEYCLOAK. Obtiene el ID generado (UUID).
        String keycloakId = keycloakServiceClient.crearUsuario(keycloakRequest);

        // 3. Mapear DTO de Request a la Entidad Cliente
        Transportista transportista = toTransportista(creationDto);
        
        // 4. Asignar el ID DE KEYCLOAK (UUID) a la Entidad local
        transportista.setKeycloak_id(keycloakId);

        // 5. Guardar la entidad en la base de datos local
        Transportista transportistaGuardado = transportistaRepository.save(transportista);

        // 6. Retornar el DTO de Respuesta
        return toDto(transportistaGuardado);
    }
    
    private CreateUserRequestDTO createKeycloakRequest(CreateTransportistaRequestDTO dto) {
        CreateUserRequestDTO request = new CreateUserRequestDTO();

        request.setUsername(dto.getDni());
        request.setEmail(dto.getEmail());
        request.setPassword(dto.getPassword());
        request.setRole("TRANSPORTISTA");
        
        return request;
    }
}
