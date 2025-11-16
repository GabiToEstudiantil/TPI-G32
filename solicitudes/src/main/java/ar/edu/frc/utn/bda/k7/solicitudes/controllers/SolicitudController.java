package ar.edu.frc.utn.bda.k7.solicitudes.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.frc.utn.bda.k7.solicitudes.clients.rutas.dtos.CrearSolicitudRequestDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.SolicitudDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.TrackingDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.TramoDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.TramoEstadoPatchDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.services.SolicitudService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/solicitudes")
@AllArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    @GetMapping
    public ResponseEntity<List<SolicitudDTO>> getSolicitudes(){
        List<SolicitudDTO> solicitudes = solicitudService.obtenerTodas();
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/{solicitudId}")
    public ResponseEntity<SolicitudDTO> solicitudesPorId(@PathVariable Integer solicitudId) {
        SolicitudDTO dto = solicitudService.buscarPorId(solicitudId);
        if (dto == null) {
        return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/cliente/{dni}")
    public ResponseEntity<List<SolicitudDTO>> solicitudesPorCliente(@PathVariable String dni) {
        List<SolicitudDTO> solicitudes = solicitudService.obtenerPorDniCliente(dni);
        
        if (solicitudes.isEmpty()) {
            return ResponseEntity.ok(solicitudes);
        }
        
        return ResponseEntity.ok(solicitudes);
    }

    @DeleteMapping("/{solicitudId}")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable Integer solicitudId) {
    SolicitudDTO existeDto = solicitudService.buscarPorId(solicitudId);
    if (existeDto == null) {
        return ResponseEntity.notFound().build();
    }
    solicitudService.eliminar(solicitudId);
    return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<SolicitudDTO> crearSolicitud(@RequestBody CrearSolicitudRequestDTO request) {
        try {
            SolicitudDTO solicitudCreada = solicitudService.crearSolicitudCompleta(request);
            return ResponseEntity.ok(solicitudCreada);
        } catch (Exception e) {
            System.err.println("Error al crear la solicitud: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{solicitudId}/tracking")
    public ResponseEntity<?> obtenerTracking(@PathVariable Integer solicitudId) {
        try {
            TrackingDTO tracking = solicitudService.trackingSolicitud(solicitudId);
            return ResponseEntity.ok(tracking);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener el tracking: " + e.getMessage());
        }
    }

    @PatchMapping("/{solicitudId}/tramos/{tramoId}/asignar-camion")
    public ResponseEntity<TramoDTO> asignarCamion (@RequestBody TramoEstadoPatchDTO tramoEstadoPatchDTO, @PathVariable Integer solicitudId, @PathVariable Integer tramoId) {
        TramoDTO tramo = solicitudService.actualizarEstadoDeTramo(tramoEstadoPatchDTO, tramoId, solicitudId);
        return ResponseEntity.ok(tramo);
    }

    @PatchMapping("/{solicitudId}/tramos/{tramoId}/desasignar-camion")
    public ResponseEntity<TramoDTO> desasignarCamion (@RequestBody TramoEstadoPatchDTO tramoEstadoPatchDTO, @PathVariable Integer solicitudId, @PathVariable Integer tramoId) {
        TramoDTO tramo = solicitudService.actualizarEstadoDeTramo(tramoEstadoPatchDTO, tramoId, solicitudId);
        return ResponseEntity.ok(tramo);
    }

    @PatchMapping("/{solicitudId}/tramos/{tramoId}/actualizar-estado")
    public ResponseEntity<TramoDTO> actualizarEstadoTramo (@RequestBody TramoEstadoPatchDTO tramoEstadoPatchDTO, @PathVariable Integer solicitudId, @PathVariable Integer tramoId) {
        TramoDTO tramo = solicitudService.actualizarEstadoDeTramo(tramoEstadoPatchDTO, tramoId, solicitudId);
        return ResponseEntity.ok(tramo);
    }

}

