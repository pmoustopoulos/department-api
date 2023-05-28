package com.ainigma100.departmentapi.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class TestHelper {

    public static String extractJsonPropertyFromFile(String filePath, String propertyName) throws IOException {

        String jsonContent = loadJsonFileContentAsString(filePath);
        return extractJsonProperty(jsonContent, propertyName);

    }


    private static String loadJsonFileContentAsString(String path) throws IOException {

        StringBuilder content = new StringBuilder();

        try (InputStream inputStream = new ClassPathResource(path).getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }

        return content.toString();
    }

    private static String extractJsonProperty(String json, String propertyName) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        JsonNode propertyNode = jsonNode.get(propertyName);

        if (propertyNode != null) {
            return propertyNode.asText();
        } else {
            return null;
        }
    }


}
