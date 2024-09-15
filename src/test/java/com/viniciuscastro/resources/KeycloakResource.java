package com.viniciuscastro.resources;

import java.util.Map;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class KeycloakResource implements QuarkusTestResourceLifecycleManager {
    KeycloakContainer container;

    @Override
    public Map<String, String> start() {
        container = new KeycloakContainer();
        container.start();

        return Map.of(
            "quarkus.oidc.auth-server-url", container.getAuthServerUrl(),
            "quarkus.oidc.client-id", "",
            "quarkus.oidc.credentials.secret", "",
            "quarkus.oidc-client.auth-server-url", container.getAuthServerUrl(),
            "quarkus.oidc-client.client-id", "",
            "quarkus.oidc-client.credentials.secret", ""
        );
    }

    @Override
    public void stop() {
        if (container != null) {
            container.stop();
        }
    }
}
