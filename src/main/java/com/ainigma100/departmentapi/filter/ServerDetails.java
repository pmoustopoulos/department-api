package com.ainigma100.departmentapi.filter;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * <p>This class listens for the ApplicationReadyEvent to log important server details such as
 * the protocol, host, port, context path, active profiles, and the URL for accessing Swagger UI.</p>
 */
@AllArgsConstructor
@Component
public class ServerDetails {

    private static final Logger log = LoggerFactory.getLogger(ServerDetails.class);


    private final Environment environment;


    @EventListener(ApplicationReadyEvent.class)
    public void logServerDetails() {

        String serverSslKeyStore = "server.ssl.key-store";
        String serverPortKey = "server.port";
        String serverServletContextPath = "server.servlet.context-path";
        String springdocSwaggerUiPath = "springdoc.swagger-ui.path";
        String defaultProfile = "default";


        String protocol = Optional.ofNullable(environment.getProperty(serverSslKeyStore)).map(key -> "https").orElse("http");
        String host = getServerIP();
        String serverPort = Optional.ofNullable(environment.getProperty(serverPortKey)).orElse("8080");
        String contextPath = Optional.ofNullable(environment.getProperty(serverServletContextPath)).orElse("");
        String[] activeProfiles = Optional.of(environment.getActiveProfiles()).orElse(new String[0]);
        String activeProfile = (activeProfiles.length > 0) ? String.join(",", activeProfiles) : defaultProfile;
        String swaggerUI = Optional.ofNullable(environment.getProperty(springdocSwaggerUiPath)).orElse("/swagger-ui/index.html");

        log.info(
                """
                        
                        
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
