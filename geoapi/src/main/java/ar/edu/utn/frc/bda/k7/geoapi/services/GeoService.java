package ar.edu.utn.frc.bda.k7.geoapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.utn.frc.bda.k7.geoapi.dto.GeoapiDTO;
import ar.edu.utn.frc.bda.k7.geoapi.dto.GeocodingDTO;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import ar.edu.utn.frc.bda.k7.geoapi.dto.RutaRequestDTO; // <-- Importar DTO
import java.util.concurrent.TimeUnit; // <-- Importar para calcular tiempo
import java.util.stream.Collectors; // <-- Importar para unir las paradas

@Service
@RequiredArgsConstructor
public class GeoService {
    
    @Value("${google.maps.apikey}")
    private String apiKey;

    private final WebClient.Builder webClientBuilder;

    public GeoapiDTO calcularDistancia(String origen, String destino) throws Exception {
        WebClient client = webClientBuilder.baseUrl("https://maps.googleapis.com/maps/api").build();

        String urlString = "/distancematrix/json?origins=" + URLEncoder.encode(origen, StandardCharsets.UTF_8.toString()) +
        "&destinations=" + URLEncoder.encode(destino, StandardCharsets.UTF_8.toString()) +
        "&units=metric&key=" + apiKey;

        String body = client.get().uri(urlString).retrieve().bodyToMono(String.class).block();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(body);
        JsonNode leg = root.path("rows").get(0).path("elements").get(0);

        GeoapiDTO dto = new GeoapiDTO();
        dto.setOrigen(origen);
        dto.setDestino(destino);
        dto.setKilometros(leg.path("distance").path("value").asDouble() / 1000);
        dto.setDuracionTexto(leg.path("duration").path("text").asText());
        return dto;
    }

public GeoapiDTO calcularRutaCompleta(RutaRequestDTO request) throws Exception {
        WebClient client = webClientBuilder.baseUrl("https://maps.googleapis.com/maps/api").build();
        
        // 1. Codificar origen y destino
        String origenEnc = URLEncoder.encode(request.getOrigen(), StandardCharsets.UTF_8.toString());
        String destinoEnc = URLEncoder.encode(request.getDestino(), StandardCharsets.UTF_8.toString());

        // 2. Codificar las paradas (waypoints)
        String waypointsEnc = "";
        if (request.getParadas() != null && !request.getParadas().isEmpty()) {
            waypointsEnc = request.getParadas().stream()
                .map(parada -> {
                    try {
                        return URLEncoder.encode(parada, StandardCharsets.UTF_8.toString());
                    } catch (Exception e) {
                        return "";
                    }
                })
                .collect(Collectors.joining("|"));
            waypointsEnc = "&waypoints=" + waypointsEnc;
        }

        // 3. Armar la URL (usando la API Directions, no Distance Matrix)
        String urlString = "/directions/json?origin=" + origenEnc +
                            "&destination=" + destinoEnc +
                            waypointsEnc + // Aquí se agregan las paradas
                            "&units=metric&key=" + apiKey;

        String body = client.get().uri(urlString).retrieve().bodyToMono(String.class).block();

        // 4. Parsear la respuesta de la API Directions
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(body);

        // La API Directions devuelve "legs" (tramos). Hay que sumarlos.
        double totalKilometros = 0;
        long totalSegundos = 0;

        JsonNode route = root.path("routes").get(0);
        for (JsonNode leg : route.path("legs")) {
            totalKilometros += leg.path("distance").path("value").asDouble();
            totalSegundos += leg.path("duration").path("value").asLong();
        }

        // 5. Formatear la duración total
        long days = TimeUnit.SECONDS.toDays(totalSegundos);
        long hours = TimeUnit.SECONDS.toHours(totalSegundos) % 24;
        long mins = TimeUnit.SECONDS.toMinutes(totalSegundos) % 60;
        String duracion = String.format("%d días, %d horas, %d minutos", days, hours, mins);
        
        // 6. Devolver el DTO de respuesta
        GeoapiDTO dto = new GeoapiDTO();
        dto.setOrigen(request.getOrigen());
        dto.setDestino(request.getDestino());
        dto.setKilometros(totalKilometros / 1000); // Convertir metros a KM
        dto.setDuracionTexto(duracion);
        return dto;
    }

    public GeocodingDTO geocode(String address) throws Exception {
        WebClient client = webClientBuilder.baseUrl("https://maps.googleapis.com/maps/api").build();

        String url = "/geocode/json?address=" + URLEncoder.encode(address, StandardCharsets.UTF_8.toString()) +
                "&key=" + apiKey;

        String body = client.get().uri(url).retrieve().bodyToMono(String.class).block();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(body);
        JsonNode result = root.path("results").get(0);

        JsonNode location = result.path("geometry").path("location");
        GeocodingDTO dto = new GeocodingDTO();
        dto.setLat(location.path("lat").asDouble());
        dto.setLng(location.path("lng").asDouble());
        dto.setFormattedAddress(result.path("formatted_address").asText());
        return dto;
    }

    public GeocodingDTO reverseGeocode(double lat, double lng) throws Exception {
        WebClient client = webClientBuilder.baseUrl("https://maps.googleapis.com/maps/api").build();

        String latlng = lat + "," + lng;
        String body = client.get()
                .uri(uriBuilder -> uriBuilder.path("/geocode/json")
                        .queryParam("latlng", latlng)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(body);
        String status = root.path("status").asText();

        if (!"OK".equals(status)) {
            String msg = root.path("error_message").asText();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Geocoding API error: " + status + (msg.isEmpty() ? "" : " - " + msg));
        }

        JsonNode results = root.path("results");
        if (results.isMissingNode() || results.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No address found for latlng: " + latlng);
        }

        JsonNode result = results.get(0);
        GeocodingDTO dto = new GeocodingDTO();
        dto.setFormattedAddress(result.path("formatted_address").asText());
        JsonNode location = result.path("geometry").path("location");
        dto.setLat(location.path("lat").asDouble());
        dto.setLng(location.path("lng").asDouble());
        return dto;
    }
}