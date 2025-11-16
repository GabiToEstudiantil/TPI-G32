package ar.edu.utn.frc.bda.k7.rutas.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.dtos.GeoapiDTO;
import ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos.CalcularDefRequestDTO;
import ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos.CalcularDefResponseDTO;
import ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos.EstimarCostoRequestDTO;
import ar.edu.utn.frc.bda.k7.rutas.clientes.solicitudes.dtos.RutaCalculadaDTO;
import ar.edu.utn.frc.bda.k7.rutas.services.RutaService;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@AllArgsConstructor
@RequestMapping("/api/ruta")
public class RutaController {

    private final RutaService rutaService;

    @PostMapping("/estimar-costo")
    public ResponseEntity<RutaCalculadaDTO> estimarCosto(@RequestBody EstimarCostoRequestDTO request) {
        
        // Validaciones básicas de entrada
        if (request.getIdOrigen() == null || request.getIdDestino() == null ||
            request.getPeso() == null || request.getVolumen() == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            RutaCalculadaDTO dto = rutaService.estimarCostoRuta(request);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            // Si algo falla (ej. Tarifa no encontrada),
            // devolvemos un 400.
            System.err.println("Error al estimar costo: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/calcular-definitivo")
    public ResponseEntity<CalcularDefResponseDTO> calcularDefinitivo(@RequestBody CalcularDefRequestDTO request) {
        try {
            CalcularDefResponseDTO dto = rutaService.calcularDefinitivo(request);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            System.err.println("Error al calcular definitivo: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    
}
