package org.example.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.example.types.APIType;

/**
 * Reads stored enviroment variables from .env file, like api keys
 * 
 * @author Antti Hakkarainen
 */
public class EnvironmentVariables {

    private static EnvironmentVariables instance;
    private static Map<APIType, String> envMap;

    private EnvironmentVariables() {        
    }

    public static synchronized EnvironmentVariables getInstance() {
        if (instance == null) {
            instance = new EnvironmentVariables();
        }
        return instance;
    }

    public static void load(String fileName) throws IOException {
        envMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                
                if (parts.length >= 2 && APIType.contains(parts[0])) {     
                    envMap.put(APIType.valueOf(parts[0]), parts[1]);
                }
                else {
                    System.err.println("Invalid data in .env at line: " + line);
                }
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(APIType apiType) {
        return envMap.get(apiType);
    }
}
