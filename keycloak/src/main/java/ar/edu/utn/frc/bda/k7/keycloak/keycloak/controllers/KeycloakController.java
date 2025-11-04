package ar.edu.utn.frc.bda.k7.keycloak.keycloak.controllers;

import ar.edu.utn.frc.bda.k7.keycloak.keycloak.dto.CreateUserRequestDTO;
import ar.edu.utn.frc.bda.k7.keycloak.keycloak.service.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
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
        // Devolvemos el ID de Keycloak
        return ResponseEntity.ok(createdUserId);
    }

    @DeleteMapping("/users/{keycloakId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String keycloakId) {
        keycloakAdminService.deleteUser(keycloakId);
        return ResponseEntity.noContent().build();
    }
}