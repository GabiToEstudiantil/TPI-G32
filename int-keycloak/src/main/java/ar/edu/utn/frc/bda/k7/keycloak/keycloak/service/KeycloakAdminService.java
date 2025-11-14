package ar.edu.utn.frc.bda.k7.keycloak.keycloak.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import ar.edu.utn.frc.bda.k7.keycloak.keycloak.dto.CreateUserRequestDTO;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Service
public class KeycloakAdminService {

    // Estas credenciales son para que TU WRAPPER se loguee a Keycloak como ADMIN
    // Deberían estar en tu application.properties, pero las hardcodeamos por simplicidad.
    private final String KEYCLOAK_SERVER_URL = "http://localhost:8081"; //
    private final String REALM = "tpi-backend";
    private final String ADMIN_CLIENT_ID = "admin-cli";
    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "admin123";

    private Keycloak getKeycloakInstance() {
        // Construye una instancia del cliente admin
        return KeycloakBuilder.builder()
                .serverUrl(KEYCLOAK_SERVER_URL)
                .realm("master") // El admin-cli se loguea contra el realm 'master'
                .clientId(ADMIN_CLIENT_ID)
                .username(ADMIN_USERNAME)
                .password(ADMIN_PASSWORD)
                .build();
    }

    public String createUser(CreateUserRequestDTO userRequest) {
        Keycloak keycloak = getKeycloakInstance();
        
        // 1. Definimos el usuario
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());

        // 2. Creamos el usuario
        Response response = keycloak.realm(REALM).users().create(user);
        
        // 3. Obtenemos el ID del usuario recién creado
        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        // 4. Definimos la contraseña
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(userRequest.getPassword());

        // 5. Seteamos la contraseña
        keycloak.realm(REALM).users().get(userId).resetPassword(passwordCred);

        // 6. Asignamos el Rol (ej. "ROLE_CLIENTE")
        RoleRepresentation role = keycloak.realm(REALM).roles().get(userRequest.getRole()).toRepresentation();
        keycloak.realm(REALM).users().get(userId).roles().realmLevel().add(List.of(role));

        return userId;
    }

    public void deleteUser(String keycloakId) {
        Keycloak keycloak = getKeycloakInstance();
        // Borra el usuario del realm 'tpi-backend'
        keycloak.realm(REALM).users().get(keycloakId).remove();
    }
}