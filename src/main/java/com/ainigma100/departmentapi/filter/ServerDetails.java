package com.ainigma100.departmentapi.filter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 * Component for logging server details upon application startup.
 *
 * This class listens for the ApplicationReadyEvent to log important server details such as
 * the protocol, host, port, context path, active profiles, and the URL for accessing Swagger UI.
 */
@Slf4j
@AllArgsConstructor
@Component
public class ServerDetails {

    private final Environment environment;
    private static final String SERVER_SSL_KEY_STORE = "server.ssl.key-store";
    private static final String SERVER_PORT = "server.port";
    private static final String SERVER_SERVLET_CONTEXT_PATH = "server.servlet.context-path";
    private static final String SPRINGDOC_SWAGGER_UI_PATH = "springdoc.swagger-ui.path";
    private static final String DEFAULT_PROFILE = "default";

    @EventListener(ApplicationReadyEvent.class)
    public void logServerDetails() {

        String protocol = Optional.ofNullable(environment.getProperty(SERVER_SSL_KEY_STORE)).map(key -> "https").orElse("http");
        String host = getServerIP();
        String serverPort = Optional.ofNullable(environment.getProperty(SERVER_PORT)).orElse("8080");
        String contextPath = Optional.ofNullable(environment.getProperty(SERVER_SERVLET_CONTEXT_PATH)).orElse("");
        String[] activeProfiles = Optional.of(environment.getActiveProfiles()).orElse(new String[0]);
        String activeProfile = (activeProfiles.length > 0) ? String.join(",", activeProfiles) : DEFAULT_PROFILE;
        String swaggerUI = Optional.ofNullable(environment.getProperty(SPRINGDOC_SWAGGER_UI_PATH)).orElse("/swagger-ui/index.html");

        log.info(
                """
                \n
                Access Swagger UI URL: {}://{}:{}{}{}
                Active Profile: {}
                """,
                protocol, host, serverPort, contextPath, swaggerUI,
                activeProfile
        );
    }

    private String getServerIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("Error resolving host address", e);
            return "unknown";
        }
    }
}
