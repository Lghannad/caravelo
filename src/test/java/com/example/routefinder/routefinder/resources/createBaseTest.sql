

-- DROP DATABASE IF EXISTS station_db_test;
-- Création de la base de données
CREATE DATABASE IF NOT EXISTS station_db_test;

-- Utilisation de la base de données
USE station_db_test;

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

-- Utilisation de la base de données
USE station_db_test;

-- Insertion des données pour les stations
INSERT INTO stations (station_code, station_name) VALUES 
('PAR', 'Paris Gare du Nord'),
('LYO', 'Lyon Part-Dieu'),
('MAR', 'Marseille Saint-Charles');

-- Insertion des données pour les alias des stations
INSERT INTO station_aliases (station_id, alias) VALUES 
(1, 'Gare du Nord'),
(1, 'Paris Nord'),
(2, 'Lyon PD'),
(2, 'Part-Dieu'),
(3, 'Marseille SC'),
(3, 'St-Charles');

-- Insertion des données pour les connexions entre stations
INSERT INTO station_connections (station_id, destination_code) VALUES 
(1, 'LYO'),
(1, 'MAR'),
(2, 'PAR'),
(2, 'MAR'),
(3, 'PAR'),
(3, 'LYO');