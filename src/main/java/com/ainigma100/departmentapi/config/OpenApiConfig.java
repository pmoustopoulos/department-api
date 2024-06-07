package com.ainigma100.departmentapi.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.io.File;
import java.io.IOException;

@Slf4j
@Configuration
public class OpenApiConfig {

    @Value("${server.port}")
    private int serverPort;

    @Bean
    public CommandLineRunner generateOpenApiJson() {

        return args -> {

            // Create RestClient instance
            RestClient restClient = RestClient.create();

            // Define the API docs URL
            String apiDocsUrl = "http://localhost:" + serverPort + "/v3/api-docs";

            try {
                // Fetch the OpenAPI JSON
                String response = restClient.get()
                        .uri(apiDocsUrl)
                        .retrieve()
                        .body(String.class);

                // Format and save the JSON to a file
                formatAndSaveToFile(response, "openapi.json");

                log.info("OpenAPI documentation generated successfully at openapi.json");

            } catch (Exception e) {
                log.error("Failed to generate OpenAPI documentation", e);
            }
        };
    }

    private void formatAndSaveToFile(String content, String fileName) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Enable pretty-print
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Read the JSON content as a JsonNode
            JsonNode jsonNode = objectMapper.readTree(content);

            // Write the formatted JSON to a file
            objectMapper.writeValue(new File(fileName), jsonNode);

        } catch (IOException e) {
            log.error("Error while saving JSON to file", e);
        }
    }
}
