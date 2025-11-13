package ar.edu.utn.frc.bda.k7.rutas.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frc.bda.k7.rutas.services.RutaService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@AllArgsConstructor
@RequestMapping("/api/ruta")
public class RutaController {

    private final RutaService rutaService;

    @PostMapping("/estimas-costo")
    public Double estimarCosto(@RequestBody String peso, String volumen, String idOrigen, String idDestino) {
        Class <?> resCamino = rutaService.construirRuta(Integer.parseInt(idOrigen), Integer.parseInt(idDestino));

        return 0.0;
    }
    
}
