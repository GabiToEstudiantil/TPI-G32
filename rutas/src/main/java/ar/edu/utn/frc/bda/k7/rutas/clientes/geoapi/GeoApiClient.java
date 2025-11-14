package ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.dtos.GeoapiDTO;
import ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.dtos.GeocodingDTO;
import ar.edu.utn.frc.bda.k7.rutas.clientes.geoapi.dtos.RutaRequestDTO;

@Service
public class GeoApiClient {

    private final RestClient restClient;

    // Inyectamos el RestClient que creamos en AppConfig
    public GeoApiClient(RestClient geoApiRestClient) {
        this.restClient = geoApiRestClient;
    }

    public GeoapiDTO obtenerRuta(RutaRequestDTO request) {
        try {
            return restClient.post()
                    .uri("/api/maps/ruta")
                    .body(request)
                    .retrieve()
                    .body(GeoapiDTO.class);
        } catch (Exception e) {
            System.err.println("Error al llamar a geoapi/ruta: " + e.getMessage());
            return null;
        }
    }

    public GeocodingDTO geocode(String address) {
        try {
            String uri = UriComponentsBuilder.fromPath("/api/maps/geocode")
                    .queryParam("address", address)
                    .toUriString();

            return restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(GeocodingDTO.class);
        } catch (Exception e) {
            System.err.println("Error al llamar a geoapi/geocode: " + e.getMessage());
            return null;
        }
    }

    public GeocodingDTO reverseGeocode(double lat, double lng) {
        try {
            String uri = UriComponentsBuilder.fromPath("/api/maps/reverse-geocode")
                    .queryParam("lat", lat)
                    .queryParam("lng", lng)
                    .toUriString();

            return restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(GeocodingDTO.class);
        } catch (Exception e) {
            System.err.println("Error al llamar a geoapi/reverse-geocode: " + e.getMessage());
            return null;
        }
    }
}
