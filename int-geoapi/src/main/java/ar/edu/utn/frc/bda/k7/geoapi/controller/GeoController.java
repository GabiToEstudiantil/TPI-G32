package ar.edu.utn.frc.bda.k7.geoapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping; // <-- 1. Importar POST
import org.springframework.web.bind.annotation.RequestBody; // <-- 2. Importar RequestBody
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frc.bda.k7.geoapi.dto.GeoapiDTO;
import ar.edu.utn.frc.bda.k7.geoapi.dto.GeocodingDTO;
import ar.edu.utn.frc.bda.k7.geoapi.dto.RutaRequestDTO; // <-- 3. Importar el DTO nuevo
import ar.edu.utn.frc.bda.k7.geoapi.services.GeoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/maps")
@RequiredArgsConstructor
public class GeoController {

    private final GeoService geoService;
    
    @GetMapping("/distancia")
    public GeoapiDTO obtenerDistancia(@RequestParam String origen, @RequestParam String destino) throws Exception {
        return geoService.calcularDistancia(origen, destino);
    }

    @PostMapping("/ruta")
    public GeoapiDTO obtenerRuta(@RequestBody RutaRequestDTO request) throws Exception {
        // 4. Llamar al nuevo método en el servicio
        return geoService.calcularRutaCompleta(request);
    }

    @GetMapping("/geocode")
    public GeocodingDTO geocode(@RequestParam String address) throws Exception {
        return geoService.geocode(address);
    }

    @GetMapping("/reverse-geocode")
    public GeocodingDTO reverseGeocode(@RequestParam double lat, @RequestParam double lng) throws Exception {
        return geoService.reverseGeocode(lat, lng);
    }
}