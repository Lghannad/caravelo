package com.example.routefinder.routefinder.utils;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.util.JSONPObject;
/**
 * Service utilitaire pour effectuer des requêtes HTTP avec Apache HttpClient
 * Cette implémentation utilise Apache HttpClient, qui offre plus de fonctionnalités que HttpURLConnection
 */
@Component
public class ReadFileUtils {
    public static JsonArray readFile(String filePath) throws Exception {
    
        String json = readFileAsString(filePath);
        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
        return jsonArray;
    
    }

    public static String readFileAsString(String file) throws Exception {
        
        return new String(Files.readAllBytes(Paths.get(file)));
    
    }
}