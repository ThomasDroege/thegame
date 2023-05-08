--- Users ---
create sequence seq_user start with 1 increment by 1;

CREATE TABLE thegame.data.users (
    user_id int NOT NULL,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) not NULL,
    PRIMARY KEY (user_id)
);

INSERT INTO thegame.data.users (user_id, first_name, last_name)
values
	(nextval('seq_user'), 'Thomas', 'Dröge'),
	(nextval('seq_user'), 'Martin', 'Schreiber'),
	(nextval('seq_user'), 'Max', 'Mustermann');
	

--- Villages ---
create sequence seq_village start with 1 increment by 1;


create table thegame.data.villages (
	village_id int not null,
	x_coords int not null,
	y_coords int not null,
	primary key (village_id),
	user_id int references thegame.data.users
);

insert into thegame.data.villages 
values 
	(nextval('seq_village'), 1, 1, 1),
	(nextval('seq_village'), 5, 10, 2);
	


--- ResourceTypes ---
create table thegame.data.resource_types (
	resource_type_id int not null,
	resource_name varchar(255) not null,
	primary key (resource_type_id)
);

insert into thegame.data.resource_types
values
	(1, 'Food'),
	(2, 'Stone'),
	(3, 'Wood'),
	(4, 'Iron');


--- Resources ---
create sequence seq_resource start with 1 increment by 1;


create table thegame.data.resources (
	resource_id int not null,
	village_id int references thegame.data.villages,
	resource_type_id int references thegame.data.resource_types,
	resource_total int not null,
	resource_income int not null,
	primary key (resource_id)
);

insert into thegame.data.resources 
values 
	(nextval('seq_resource'), 1, 1, 1000, 20),
	(nextval('seq_resource'), 1, 2, 200, 5),
	(nextval('seq_resource'), 1, 3, 400, 20),
	(nextval('seq_resource'), 1, 4, 50, 0),
	(nextval('seq_resource'), 2, 1, 1000, 20),
	(nextval('seq_resource'), 2, 2, 200, 5),
	(nextval('seq_resource'), 2, 3, 400, 20),
	(nextval('seq_resource'), 2, 4, 50, 0);