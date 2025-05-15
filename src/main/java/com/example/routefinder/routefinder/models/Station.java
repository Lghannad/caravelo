package com.example.routefinder.routefinder.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Entity class representing a station in the database.
 */
public class Station implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String stationCode;
    private String stationName;
    private List<String> aliases;
    private List<String> connexions;
    
    public List<String> getConnexions() {
        return connexions;
    }

    public void setConnexions(List<String> connexions) {
        this.connexions = connexions;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }
    public void addAliases(String alias){
        this.aliases.add(alias);
    }

    /**
     * Default constructor.
     */
    public Station() {
    }
    
    /**
     * Constructor with all fields except ID (which is auto-generated).
     * 
     * @param stationCode Station code (limited to 3 characters)
     * @param stationName Station name
     */
    public Station(String stationCode, String stationName,List<String> aliases) {
        this.stationCode = stationCode;
        this.stationName = stationName;
        this.aliases = aliases;

            
    }
    
    /**
     * Complete constructor with all fields.
     * 
     * @param id Station ID (auto-generated in database)
     * @param stationCode Station code (limited to 3 characters)
     * @param stationName Station name
     */
    public Station(Integer id, String stationCode, String stationName,List<String> aliases) {
        this.id = id;
        this.stationCode = stationCode;
        this.stationName = stationName;
        this.aliases = aliases;
    }
    
    // Getters and Setters
    
    /**
     * @return the station ID
     */
    public Integer getId() {
        return id;
    }
    
    /**
     * @param id the ID to set
     */
    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * @return the station code
     */
    public String getStationCode() {
        return stationCode;
    }
    
    /**
     * @param stationCode the station code to set (limited to 3 characters)
     * @throws IllegalArgumentException if the code exceeds 3 characters
     */
    public void setStationCode(String stationCode) {
        if (stationCode != null && stationCode.length() > 3) {
            throw new IllegalArgumentException("Station code cannot exceed 3 characters");
        }
        this.stationCode = stationCode;
    }
    
    /**
     * @return the station name
     */
    public String getStationName() {
        return stationName;
    }
    
    /**
     * @param stationName the station name to set
     */
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(id, station.id) &&
                Objects.equals(stationCode, station.stationCode) &&
                Objects.equals(stationName, station.stationName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, stationCode, stationName);
    }

    @Override
    public String toString() {
        return "Station [id=" + id + ", stationCode=" + stationCode + ", stationName=" + stationName + ", aliases="
                + aliases + ", connexions=" + connexions + "]";
    }
    
   
}