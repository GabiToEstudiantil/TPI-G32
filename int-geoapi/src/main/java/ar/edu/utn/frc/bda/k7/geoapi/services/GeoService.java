package ar.edu.utn.frc.bda.k7.geoapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import ar.edu.utn.frc.bda.k7.geoapi.dto.GeoapiDTO;
import ar.edu.utn.frc.bda.k7.geoapi.dto.GeocodingDTO;
import ar.edu.utn.frc.bda.k7.geoapi.dto.RutaRequestDTO;
import ar.edu.utn.frc.bda.k7.geoapi.dto.google.*;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeoService {
    
    @Value("${google.maps.apikey}")
    private String apiKey;

    private final RestClient googleMapsRestClient; // Inyectado desde AppConfig

    public GeoapiDTO calcularDistancia(String origen, String destino) {
        
        // Llama a la API y deserializa automáticamente en GoogleDistanceResponse
        GoogleDistanceResponseDTO response = googleMapsRestClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/distancematrix/json")
                    .queryParam("origins", origen)
                    .queryParam("destinations", destino)
                    .queryParam("units", "metric")
                    .queryParam("key", apiKey)
                    .build())
                .retrieve()
                .body(GoogleDistanceResponseDTO.class);

        // Mapeo seguro y limpio desde los DTOs
        GoogleDistanceElementDTO leg = response.getRows().get(0).getElements().get(0);

        GeoapiDTO dto = new GeoapiDTO();
        dto.setOrigen(origen);
        dto.setDestino(destino);
        dto.setKilometros(leg.getDistance().getValue() / 1000);
        dto.setDuracionTexto(leg.getDuration().getText());
        return dto;
    }

    public GeoapiDTO calcularRutaCompleta(RutaRequestDTO request) {

        // 1. Unir las paradas con "|" (sin codificar manualmente)
        String waypointsStr = "";
        if (request.getParadas() != null && !request.getParadas().isEmpty()) {
            waypointsStr = request.getParadas().stream()
                .collect(Collectors.joining("|"));
        }

        final String finalWaypointsStr = waypointsStr;

        // 2. Llamar a la API (RestClient codificará los parámetros)
        GoogleDirectionsResponseDTO response = googleMapsRestClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/directions/json")
                        .queryParam("origin", request.getOrigen())
                        .queryParam("destination", request.getDestino())
                        .queryParam("units", "metric")
                        .queryParam("key", apiKey);
                    
                    if (!finalWaypointsStr.isEmpty()) {
                        uriBuilder.queryParam("waypoints", finalWaypointsStr);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .body(GoogleDirectionsResponseDTO.class);

        // 3. Parsear la respuesta desde los DTOs
        double totalKilometros = 0;
        long totalSegundos = 0;

        // Sumamos los "legs" (tramos) de la primera ruta encontrada
        for (GoogleDirectionsLegDTO leg : response.getRoutes().get(0).getLegs()) {
            totalKilometros += leg.getDistance().getValue();
            totalSegundos += leg.getDuration().getValue();
        }

        // 4. Formatear la duración total
        long days = TimeUnit.SECONDS.toDays(totalSegundos);
        long hours = TimeUnit.SECONDS.toHours(totalSegundos) % 24;
        long mins = TimeUnit.SECONDS.toMinutes(totalSegundos) % 60;
        String duracion = String.format("%d días, %d horas, %d minutos", days, hours, mins);
        
        // 5. Devolver el DTO de respuesta
        GeoapiDTO dto = new GeoapiDTO();
        dto.setOrigen(request.getOrigen());
        dto.setDestino(request.getDestino());
        dto.setKilometros(totalKilometros / 1000); // Convertir metros a KM
        dto.setDuracionTexto(duracion);
        return dto;
    }

    public GeocodingDTO geocode(String address) {
        
        GoogleGeocodingResponseDTO response = googleMapsRestClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/geocode/json")
                    .queryParam("address", address)
                    .queryParam("key", apiKey)
                    .build())
                .retrieve()
                .body(GoogleGeocodingResponseDTO.class);

        // (Aquí puedes agregar manejo de errores si response.getResults() está vacío)
        GoogleGeocodingResultDTO result = response.getResults().get(0);

        GeocodingDTO dto = new GeocodingDTO();
        dto.setLat(result.getGeometry().getLocation().getLat());
        dto.setLng(result.getGeometry().getLocation().getLng());
        dto.setFormattedAddress(result.getFormatted_address());
        return dto;
    }

    public GeocodingDTO reverseGeocode(double lat, double lng) {

        GoogleGeocodingResponseDTO response = googleMapsRestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/geocode/json")
                        .queryParam("latlng", lat + "," + lng)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .body(GoogleGeocodingResponseDTO.class);

        // Manejo de errores usando los DTOs
        if (!"OK".equals(response.getStatus())) {
            String msg = response.getError_message();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Geocoding API error: " + response.getStatus() + (msg == null ? "" : " - " + msg));
        }

        if (response.getResults() == null || response.getResults().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No address found for latlng: " + lat + "," + lng);
        }

        GoogleGeocodingResultDTO result = response.getResults().get(0);
        GeocodingDTO dto = new GeocodingDTO();
        dto.setFormattedAddress(result.getFormatted_address());
        dto.setLat(result.getGeometry().getLocation().getLat());
        dto.setLng(result.getGeometry().getLocation().getLng());
        return dto;
    }
}