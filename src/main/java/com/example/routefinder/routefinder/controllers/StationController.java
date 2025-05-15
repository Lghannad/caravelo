package com.example.routefinder.routefinder.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.routefinder.routefinder.services.StationService;

/**
 * REST controller for handling station-related API endpoints.
 * Provides functionality for updating the database and retrieving routes.
 */
@RestController
@RequestMapping("/station")
public class StationController {

    private static final Logger logger  = LoggerFactory.getLogger(StationController.class);

    /**
     * Updates the station database from specified data source.
     * 
     * @param documentSources Source of station data ("internal" or "external")
     * @return ResponseEntity with status and message
     * @throws Exception If there is an error during the update process
     */
    @GetMapping("update")
    public ResponseEntity updateDatabase(@RequestParam String documentSources) throws Exception {
         logger.info("Calling /update for this API : " + documentSources  );
          Connection conn = null;
        conn = DriverManager.getConnection("jdbc:mysql://localhost/station_db?" +
                                            "user=root&password=root");
         switch (documentSources){
            case "internal":
                try {
                        StationService.updateFirst(conn);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                break;
            case "external":
            logger.warn("external support not yet implemented");
            return ResponseEntity.status(500).body("external support not yet implemented");
            default:
                return ResponseEntity.status(500).body(documentSources + " API support not yet implemented");
        }
    return ResponseEntity.status(200).body("database updated");
    }
    
    /**
     * Retrieves available routes from a specific station.
     * 
     * @param param Name or alias of the origin station
     * @return ResponseEntity with list of connected stations or error message
     * @throws SQLException If there is a database error
     */
    @GetMapping("routes")
    public ResponseEntity  getRoutesFromStation(@RequestParam String param) throws SQLException {
        ResponseEntity responseEntity = null;
        Connection conn = null;

        logger.info("Calling /routes");
        List<String> connectionRoutes = new ArrayList<>();
        conn = DriverManager.getConnection("jdbc:mysql://localhost/station_db?" +"user=root&password=root");
        try {
            connectionRoutes =  StationService.getRoutesFromStation(param,conn);
            return ResponseEntity.ok(connectionRoutes);
        } catch (Exception e) {
            logger.error("Error while calling /routes :"+ e);
            return ResponseEntity.status(500).body("error");
        }
    }
}