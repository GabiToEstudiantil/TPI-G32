package ar.edu.utn.frc.bda.k7.users.service;


import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ar.edu.utn.frc.bda.k7.users.domain.Transportista;
import ar.edu.utn.frc.bda.k7.users.dto.TransportistaDTO;
import ar.edu.utn.frc.bda.k7.users.repository.TransportistaRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransportistaService {
    
    private final TransportistaRepository transportistaRepository;

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

    public Transportista toTransportista(TransportistaDTO dto) {
        Transportista transportista = new Transportista();
        transportista.setLegajo(dto.getLegajo());
        transportista.setDni(dto.getDni());
        transportista.setNombre(dto.getNombre());
        transportista.setApellido(dto.getApellido());
        transportista.setTelefono(dto.getTelefono());
        transportista.setEmail(dto.getEmail());
        transportista.setKeycloak_id(dto.getKeycloak_id());
        return transportista;
    }

    public ArrayList<TransportistaDTO> obtenerTodos(){
        ArrayList<TransportistaDTO> transportistaDTO = new ArrayList<>();
        for (Transportista transportista : transportistaRepository.findAll()){
            transportistaDTO.add(toDto(transportista));
        }
        return transportistaDTO;
    }

    public TransportistaDTO buscarPorLegajo(Integer legajo){
        return toDto(transportistaRepository.findById(legajo).orElse(null));
    }

    @Transactional
    public Transportista crearTransportista(TransportistaDTO dto){
        return transportistaRepository.save(toTransportista(dto));
    }

    @Transactional
    public TransportistaDTO actualizar(Integer legajo, TransportistaDTO dto) {
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
}




   
