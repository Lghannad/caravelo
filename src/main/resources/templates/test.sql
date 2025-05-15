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