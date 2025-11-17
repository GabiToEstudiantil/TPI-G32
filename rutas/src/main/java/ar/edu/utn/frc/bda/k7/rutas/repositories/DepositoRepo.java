package ar.edu.utn.frc.bda.k7.rutas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.utn.frc.bda.k7.rutas.entities.Deposito;

public interface DepositoRepo extends JpaRepository<Deposito, Integer> {
    Deposito findByUbicacionId(Integer ubicacionId);
}
