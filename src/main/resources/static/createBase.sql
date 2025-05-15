-- Création de la base de données
CREATE DATABASE IF NOT EXISTS station_db;

-- Utilisation de la base de données
USE station_db;

-- Création de la table principale
CREATE TABLE stations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    station_code VARCHAR(3) NOT NULL,
    station_name VARCHAR(50) NOT NULL
);

-- Ajout d'un index sur stationCode pour améliorer les performances de recherche
CREATE INDEX idx_station_code ON stations(station_code);

-- Création de la table de liaison pour les connexions
CREATE TABLE station_connections (
    station_id INT NOT NULL,
    destination_code VARCHAR(3) NOT NULL,
    FOREIGN KEY (station_id) REFERENCES stations(id) ON DELETE CASCADE
);

-- Ajout d'un index sur la clé étrangère station_id pour les connexions
CREATE INDEX idx_conn_station_id ON station_connections(station_id);

-- Création de la table de liaison pour les alias
CREATE TABLE station_aliases (
    station_id INT NOT NULL,
    alias VARCHAR(100) NOT NULL,
    FOREIGN KEY (station_id) REFERENCES stations(id) ON DELETE CASCADE
);

-- Ajout d'un index sur la clé étrangère station_id pour les alias
CREATE INDEX idx_alias_station_id ON station_aliases(station_id);