package ar.edu.utn.frc.bda.k7.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.utn.frc.bda.k7.users.domain.Cliente;

public interface ClientRepository extends JpaRepository <Cliente, String>{

    //comentario como hizo gabi

} 

