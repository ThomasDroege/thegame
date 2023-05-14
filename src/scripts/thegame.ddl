--- Users ---
CREATE SEQUENCE seq_user
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
	(nextval('seq_user'), 'Thomas', 'Dröge'),
	(nextval('seq_user'), 'Martin', 'Schreiber'),
	(nextval('seq_user'), 'Max', 'Mustermann');
	

--- Villages ---
CREATE SEQUENCE seq_village
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
	(nextval('seq_village'), 1, 1, 1),
	(nextval('seq_village'), 5, 10, 2);
	


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
CREATE SEQUENCE seq_resource
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
	(nextval('seq_resource'), 1, 1, 1000, 100),
	(nextval('seq_resource'), 1, 2, 400, 100),
	(nextval('seq_resource'), 1, 3, 200, 25),
	(nextval('seq_resource'), 1, 4, 50, 0),
	(nextval('seq_resource'), 2, 1, 1000, 100),
	(nextval('seq_resource'), 2, 2, 400, 100),
	(nextval('seq_resource'), 2, 3, 200, 25),
	(nextval('seq_resource'), 2, 4, 50, 0);
