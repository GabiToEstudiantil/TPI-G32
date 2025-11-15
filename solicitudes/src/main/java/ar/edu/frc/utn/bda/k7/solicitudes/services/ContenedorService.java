package ar.edu.frc.utn.bda.k7.solicitudes.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.frc.utn.bda.k7.solicitudes.domain.Contenedor;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.ContenedorDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.repositories.ContenedorRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ContenedorService {

    private final ContenedorRepo contenedorRepo;

    // Mappers
    public ContenedorDTO toDto(Contenedor contenedor) {
        ContenedorDTO dto = new ContenedorDTO();
        dto.setId(contenedor.getId());
        dto.setCodigoIdentificacion(contenedor.getCodigoIdentificacion());
        dto.setPeso(contenedor.getPeso());
        dto.setVolumen(contenedor.getVolumen());
        dto.setEstado(contenedor.getEstado());
        dto.setClienteDni(contenedor.getClienteDni());
        return dto;
    }

    public Contenedor toContenedor(ContenedorDTO dto) {
        Contenedor contenedor = new Contenedor();
        contenedor.setId(dto.getId());
        contenedor.setCodigoIdentificacion(dto.getCodigoIdentificacion());
        contenedor.setPeso(dto.getPeso());
        contenedor.setVolumen(dto.getVolumen());
        contenedor.setEstado(dto.getEstado());
        contenedor.setClienteDni(dto.getClienteDni());
        return contenedor;
    }
    // FIN Mappers

    @Transactional
    public Contenedor save(Contenedor contenedor) {
        return contenedorRepo.save(contenedor);
    }
}
