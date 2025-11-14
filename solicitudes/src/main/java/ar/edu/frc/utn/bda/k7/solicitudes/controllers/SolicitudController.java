package ar.edu.frc.utn.bda.k7.solicitudes.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.frc.utn.bda.k7.solicitudes.dtos.SolicitudDTO;
import ar.edu.frc.utn.bda.k7.solicitudes.services.SolicitudService;

@RestController
@RequestMapping("/api/solicitudes/")
public class SolicitudController {

    private SolicitudService solicitudService;

    @GetMapping
    public List<SolicitudDTO> getSolicitudes(){
        return solicitudService.obtenerTodas();
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
    
}



    

