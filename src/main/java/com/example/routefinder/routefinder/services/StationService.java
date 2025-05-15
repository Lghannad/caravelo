package com.example.routefinder.routefinder.services;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.routefinder.routefinder.models.Station;

import com.example.routefinder.routefinder.utils.ReadFileUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class StationService {
    private static final Logger logger  = LoggerFactory.getLogger(StationService.class);

    /**
     * Reads station data from a JSON file and updates the database.
     * 
     * @param conn Database connection
     * @throws Exception If there is an error during update process
     */
    public static void updateFirst(Connection conn) throws Exception {
        List<Station> stationsList;
       
        String filePath = "src\\main\\resources\\templates\\InternalClientData.json";
        JsonArray content = ReadFileUtils.readFile(filePath);
        stationsList = treatJsonArray(content);
        publishStation(stationsList,conn);
    }

    /**
     * Converts a JSON array into a list of Station objects.
     * 
     * @param stationArray JSON array containing station data
     * @return List of Station objects
     */
    public static List<Station> treatJsonArray(JsonArray stationArray){
        List<Station> stationList = new ArrayList<>();
        for(JsonElement o: stationArray){
            
            String aliasesString;
            List<String> listAlias = new ArrayList<>();
            List<String> connexionList = new ArrayList<>();
            JsonArray connections = null;
            JsonObject routes;
            JsonObject jsonObjectStation = o.getAsJsonObject();

            routes = jsonObjectStation.get("route").getAsJsonObject();

            if (routes.get("connection").isJsonArray()){
                connections = routes.get("connection").getAsJsonArray();
            }
            if (connections != null){
                for (JsonElement connection: connections){
                    connexionList.add(connection.getAsJsonObject().get("arrivalStation").toString().replace("\"", ""));
                }
        }
            aliasesString = jsonObjectStation.get("alias").toString().replace("\"", "");
            listAlias = Arrays.asList(aliasesString.split(","));

            Station stationData = new Station(jsonObjectStation.get("stationCode").toString()
            ,jsonObjectStation.get("stationName").toString(),listAlias);
            stationData.setConnexions(connexionList);
            stationList.add(stationData);
        }
        return stationList;
    }

    /**
     * Inserts a list of stations into the database.
     * 
     * @param stationsList List of Station objects to insert
     * @param conn Database connection
     * @throws SQLException If there is a database error
     */
    public static void publishStation(List<Station> stationsList,Connection conn) throws SQLException{
        
                                                    Statement stmt = conn.createStatement();


        for (Station station : stationsList){
                stmt.executeUpdate("INSERT INTO stations (station_code, station_name) VALUES (" +station.getStationCode() + ","+station.getStationName()+")",Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                Integer pk = rs.getInt(1);

                for (String aliasvalue: station.getAliases()){
                            stmt.executeUpdate("INSERT INTO station_aliases (station_id, alias) VALUES (" +pk + ",'"+ aliasvalue +"')");
                }
                for (String conectionValue: station.getConnexions()){
                    //System.out.println(conectionValue);
                    stmt.executeUpdate("INSERT INTO station_connections (station_id, destination_code) VALUES (" +pk + ",'"+ conectionValue +"')");
            }
        }
        conn.close();
    }

    /**
     * Gets routes available from a specific station.
     * 
     * @param originStation Name or alias of the origin station
     * @param conn Database connection
     * @return List of connected station names
     * @throws Exception If station is not found or empty
     */
    public static List<String> getRoutesFromStation(String originStation,Connection conn) throws Exception {

//Connection conn = null;
        Integer stationId;
        List<String> connections;
        
        stationId = getIdFromAlias(originStation, conn);
        if (stationId == 0){
            logger.error("Empty value found in originStation parameter");
            conn.close();
            throw new Exception("No station");
        }
         if (stationId == -1){
            logger.error("no stations found for this alias : " + originStation);
            conn.close();
            throw new Exception("Unknown station");
        }
        connections = getConnectionFromId(stationId, conn);
        conn.close();
        return connections; 
    }

    /**
     * Gets all connections for a station by its ID.
     * 
     * @param stationId ID of the station
     * @param conn Database connection
     * @return List of connected station names
     * @throws SQLException If there is a database error
     */
     public static List<String> getConnectionFromId(Integer stationId, Connection conn) throws SQLException {
        List<String> stationConnections = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet getNewVal = stmt.executeQuery("SELECT station_name FROM stations WHERE stations.station_code IN (SELECT destination_code FROM station_connections WHERE station_connections.station_id =" +stationId+");");
        if (!getNewVal.isBeforeFirst() ) {    
            return null;
            
        }
        while (getNewVal.next()) {
            stationConnections.add(getNewVal.getString(1).toString());
        }
    return stationConnections;
        
    } 

    /**
     * Gets station ID from a station alias.
     * 
     * @param originStation Alias of the station
     * @param conn Database connection
     * @return Station ID (0 if empty, -1 if not found)
     * @throws SQLException If there is a database error
     */
    public static Integer getIdFromAlias(String originStation, Connection conn)throws SQLException {

            Statement stmt = conn.createStatement();
            if (originStation ==""){
            return 0;
        }
        ResultSet getNewVal = stmt.executeQuery("SELECT station_id FROM stations JOIN station_aliases as a WHERE a.alias = '"+originStation+"' LIMIT 1");
       
        if (!getNewVal.isBeforeFirst() ) {    
            return -1; 
} 
        getNewVal.next();
        return getNewVal.getInt(1);    }
}