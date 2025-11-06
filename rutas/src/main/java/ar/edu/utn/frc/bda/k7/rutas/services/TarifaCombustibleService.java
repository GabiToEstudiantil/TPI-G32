package ar.edu.utn.frc.bda.k7.rutas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.utn.frc.bda.k7.rutas.repositories.TarifaCombustibleRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TarifaCombustibleService {

    @Autowired
    private TarifaCombustibleRepo tarifaCombustibleRepo;
}
