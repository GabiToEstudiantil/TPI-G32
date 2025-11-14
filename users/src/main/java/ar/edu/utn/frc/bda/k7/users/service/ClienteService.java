package ar.edu.utn.frc.bda.k7.users.service;

import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ar.edu.utn.frc.bda.k7.users.repository.ClientRepository;
import lombok.AllArgsConstructor;
import ar.edu.utn.frc.bda.k7.users.domain.*;
import ar.edu.utn.frc.bda.k7.users.dto.ClienteDTO;


@Service
@AllArgsConstructor
public class ClienteService {

    private final ClientRepository clienteRepository;

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

    public ClienteDTO buscarPorDni(Integer dni){
        return toDto(clienteRepository.findById(dni).orElse(null));
    }

    @Transactional
    public Cliente crearCliente(ClienteDTO dto){
        return clienteRepository.save(toCliente(dto));
    }

    @Transactional
    public ClienteDTO actualizar(Integer dni, ClienteDTO dto) {
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

} 

