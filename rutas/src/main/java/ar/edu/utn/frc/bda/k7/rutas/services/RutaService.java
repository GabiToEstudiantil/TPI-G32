package ar.edu.utn.frc.bda.k7.rutas.services;

import org.springframework.stereotype.Service;

import ar.edu.utn.frc.bda.k7.rutas.dtos.UbicacionDTO;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RutaService {

    private final UbicacionService ubicacionService;
    private final TarifaCombustibleService tarifaCombustibleService;

    public Class<?> construirRuta(Integer idOrigen, Integer idDestino) {
        UbicacionDTO origen = ubicacionService.getUbicacionById(idOrigen);
        UbicacionDTO destino = ubicacionService.getUbicacionById(idDestino);
        this.llamarMsMaps(origen, destino);
        return Object.class;
    }

    private String llamarMsMaps(UbicacionDTO origen, UbicacionDTO destino) {
        // Lógica para llamar al microservicio de Maps y obtener la ruta
        return "";
        // Por simplicidad, retornamos una cadena simulada
    }
}
