--- Data Base ---
CREATE DATABASE thegame
OWNER = postgres
ENCODING = 'UTF8'
TABLESPACE = pg_default;

--- Data Schema ---
CREATE SCHEMA "data"
AUTHORIZATION postgres;


--- Users ---
CREATE SEQUENCE thegame.data.seq_user
	START WITH 1
	INCREMENT BY 1;

CREATE TABLE thegame.data.users (
    user_id int NOT NULL,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    PRIMARY KEY (user_id)
);

INSERT INTO thegame.data.users (user_id, first_name, last_name)
VALUES
	(nextval('thegame.data.seq_user'), 'Thomas', 'Dröge'),
	(nextval('thegame.data.seq_user'), 'Martin', 'Schreiber'),
	(nextval('thegame.data.seq_user'), 'Max', 'Mustermann');


--- Villages ---
CREATE SEQUENCE thegame.data.seq_village
	START WITH 1
	INCREMENT BY 1;

CREATE TABLE thegame.data.villages (
	village_id int NOT NULL,
	x_coords int NOT NULL,
	y_coords int NOT NULL,
	primary key (village_id),
	user_id int references thegame.data.users
);

INSERT INTO thegame.data.villages
VALUES
	(nextval('thegame.data.seq_village'), 1, 1, 1),
	(nextval('thegame.data.seq_village'), 5, 10, 2);


--- ResourceTypes ---
CREATE TABLE thegame.data.resource_types (
	resource_type_id int NOT NULL,
	resource_name varchar(255) NOT NULL,
	primary key (resource_type_id)
);

INSERT INTO thegame.data.resource_types
VALUES
	(1, 'Food'),
	(2, 'Wood'),
	(3, 'Stone'),
	(4, 'Iron');


--- Resources ---
CREATE SEQUENCE thegame.data.seq_resource
	START WITH 1
	INCREMENT BY 1;

CREATE TABLE thegame.data.resources (
	resource_id int NOT NULL,
	village_id int references thegame.data.villages,
	resource_type_id int references thegame.data.resource_types,
	resource_at_update_time int NOT NULL,
	resource_income int NOT NULL,
	update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	primary key (resource_id)
);

INSERT INTO thegame.data.resources
VALUES
	(nextval('thegame.data.seq_resource'), 1, 1, 1000, 100),
	(nextval('thegame.data.seq_resource'), 1, 2, 400, 100),
	(nextval('thegame.data.seq_resource'), 1, 3, 200, 25),
	(nextval('thegame.data.seq_resource'), 1, 4, 50, 0),
	(nextval('thegame.data.seq_resource'), 2, 1, 1000, 100),
	(nextval('thegame.data.seq_resource'), 2, 2, 400, 100),
	(nextval('thegame.data.seq_resource'), 2, 3, 200, 25),
	(nextval('thegame.data.seq_resource'), 2, 4, 50, 0);


--- Building Types ---
CREATE TABLE thegame.data.building_types (
	building_type_id int NOT NULL,
	building_name varchar(255) NOT NULL,
	primary key (building_type_id)
);

INSERT INTO thegame.data.building_types
VALUES
	(1, 'City Hall'),
	(2, 'Mill'),
	(3, 'Lumberjack'),
	(4, 'Mason'),
	(5, 'Iron Mine');


--- Buildings ---
CREATE SEQUENCE thegame.data.seq_building
	START WITH 1
	INCREMENT BY 1;

CREATE TABLE thegame.data.buildings2 (
	building_id int NOT NULL,
	village_id int references thegame.data.villages,
	building_type_id int references thegame.data.building_types,
	building_level int NOT NULL,
	update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

	primary key (building_id)
);

INSERT INTO thegame.data.buildings2
VALUES
	(nextval('thegame.data.seq_building'), 1, 1, 1),
	(nextval('thegame.data.seq_building'), 1, 2, 1),
	(nextval('thegame.data.seq_building'), 1, 3, 1),
	(nextval('thegame.data.seq_building'), 1, 4, 1),
	(nextval('thegame.data.seq_building'), 1, 5, 0),
	(nextval('thegame.data.seq_building'), 2, 1, 1),
	(nextval('thegame.data.seq_building'), 2, 2, 1),
	(nextval('thegame.data.seq_building'), 2, 3, 1),
	(nextval('thegame.data.seq_building'), 2, 4, 1),
	(nextval('thegame.data.seq_building'), 2, 5, 0);

---------------------------------------------------------------------------------------------------------------
------ Wird vermutlich nicht mehr benötigt, da Effect und Building Level über Property Files gelöst wird ------
---------------------------------------------------------------------------------------------------------------

--- Effect ---
CREATE SEQUENCE thegame.data.seq_effect
	START WITH 1
	INCREMENT BY 1;

CREATE TABLE thegame.data.effect (
	effect_id int NOT NULL,
	effect_name varchar(255) NOT NULL,
	primary key (effect_id)
);

INSERT INTO thegame.data.effect
VALUES
	(nextval('thegame.data.seq_effect'), 1, 1, 1),
	(nextval('thegame.data.seq_building'), 1, 2, 1),
	(nextval('thegame.data.seq_building'), 1, 3, 1),
	(nextval('thegame.data.seq_building'), 1, 4, 1),
	(nextval('thegame.data.seq_building'), 1, 5, 0),
	(nextval('thegame.data.seq_building'), 2, 1, 1),
	(nextval('thegame.data.seq_building'), 2, 2, 1),
	(nextval('thegame.data.seq_building'), 2, 3, 1),
	(nextval('thegame.data.seq_building'), 2, 4, 1),
	(nextval('thegame.data.seq_building'), 2, 5, 0);




--- Building Level ---
CREATE SEQUENCE thegame.data.seq_building_level
	START WITH 1
	INCREMENT BY 1;

CREATE TABLE thegame.data.building_level (
	building_level_id int NOT NULL,
	building_type_id int references thegame.data.building_types,
	building_level int NOT NULL,
	construction_time int NOT NULL,
	upgrade_costs_food int NOT NULL,
	upgrade_costs_wood int NOT NULL,
	upgrade_costs_stone int NOT NULL,
	upgrade_costs_iron int NOT NULL,
	income_food int NOT NULL,
	income_wood int NOT NULL,
	income_stone int NOT NULL,
	income_iron int NOT NULL,
	effect_multiplier double NOT NULL,
	effect_total int NOT NULL,
	effect_id references the.game.data.effect,
	primary key (building_level_id)
);