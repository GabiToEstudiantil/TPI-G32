package ar.edu.utn.frc.bda.k7.keycloak.keycloak.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ar.edu.utn.frc.bda.k7.keycloak.keycloak.dto.CreateUserRequestDTO;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Service
public class KeycloakAdminService {

    @Value("${keycloak.admin.url}")
    private String keycloakServerUrl; // (http://localhost:8081)

    @Value("${keycloak.admin.realm}")
    private String realm; // (tpi-backend)

    @Value("${keycloak.admin.client-id}")
    private String adminClientId; // (admin-cli)

    @Value("${keycloak.admin.username}")
    private String adminUsername; // (admin)

    @Value("${keycloak.admin.password}")
    private String adminPassword; // (admin123)


    private Keycloak getKeycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakServerUrl)
                .realm("master")
                .clientId(adminClientId)
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }

    public String createUser(CreateUserRequestDTO userRequest) {
        Keycloak keycloak = getKeycloakInstance();
        
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());

        Response response = keycloak.realm(realm).users().create(user);
        
        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(userRequest.getPassword());

        keycloak.realm(realm).users().get(userId).resetPassword(passwordCred);

        RoleRepresentation role = keycloak.realm(realm).roles().get(userRequest.getRole()).toRepresentation();
        keycloak.realm(realm).users().get(userId).roles().realmLevel().add(List.of(role));

        return userId;
    }

    public void deleteUser(String keycloakId) {
        Keycloak keycloak = getKeycloakInstance();
        keycloak.realm(realm).users().get(keycloakId).remove();
    }
}