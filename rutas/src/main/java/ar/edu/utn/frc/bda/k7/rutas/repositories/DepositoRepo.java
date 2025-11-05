package ar.edu.utn.frc.bda.k7.rutas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.utn.frc.bda.k7.rutas.entities.Deposito;

public interface DepositoRepo extends JpaRepository<Deposito, Integer> {
    // Aca un comentario pq queda feo completamente vacio hasta que le agreguemos alguno personalizado
    
}
