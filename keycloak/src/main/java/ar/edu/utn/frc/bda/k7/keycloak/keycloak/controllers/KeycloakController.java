package ar.edu.utn.frc.bda.k7.keycloak.keycloak.controllers;

import ar.edu.utn.frc.bda.k7.keycloak.keycloak.dto.CreateUserRequestDTO;
import ar.edu.utn.frc.bda.k7.keycloak.keycloak.service.KeycloakAdminService;
import lombok.RequiredArgsConstructor;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") //
@RequiredArgsConstructor
public class KeycloakController {

    private final KeycloakAdminService keycloakAdminService;

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody CreateUserRequestDTO userRequest) {
        String createdUserId = keycloakAdminService.createUser(userRequest);
        // // Devolvemos el ID de Keycloak
        // return ResponseEntity.ok(createdUserId);
        // 1. Crear la URI del recurso recién creado
        // Ruta: /api/auth/users/{UUID-GENERADO}
        URI location = URI.create("/api/auth/users/" + createdUserId);
        
        // 2. Retorna 201 Created y el header Location
        return ResponseEntity.created(location).build();
        // NOTA: Cambiamos a ResponseEntity<Void> para indicar que el cuerpo está vacío, 
        // ya que la información del recurso está en el header Location.h
    }

    @DeleteMapping("/users/{keycloakId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String keycloakId) {
        keycloakAdminService.deleteUser(keycloakId);
        return ResponseEntity.noContent().build();
    }
}