package ar.edu.utn.frc.bda.k7.rutas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.utn.frc.bda.k7.rutas.repositories.CamionRepo;
import ar.edu.utn.frc.bda.k7.rutas.repositories.CiudadRepo;
import ar.edu.utn.frc.bda.k7.rutas.repositories.DepositoRepo;
import ar.edu.utn.frc.bda.k7.rutas.repositories.TarifaCombustibleRepo;
import ar.edu.utn.frc.bda.k7.rutas.repositories.TarifaVolumenRepo;
import ar.edu.utn.frc.bda.k7.rutas.repositories.UbicacionRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RutaService {

    @Autowired
    private final CamionRepo camionRepo;
    @Autowired
    private final CiudadRepo ciudadRepo;
    @Autowired
    private final TarifaVolumenRepo tarifaVolumenRepo;
    @Autowired
    private final DepositoRepo depositoRepo;
    @Autowired
    private final TarifaCombustibleRepo tarifaCombustibleRepo;
    @Autowired
    private final UbicacionRepo ubicacionRepo;

    
}
