use finding_wildlife;

create table park (
	park_id int primary key auto_increment,
    park_name varchar(250) not null,
    center_latitude decimal(10,6) not null,
    center_longitude decimal(10,6) not null
);

create table trail (
	trail_id int primary key auto_increment,
    trail_name varchar(250) null,
    trail_number int null,
    trail_distance decimal(10,2) not null,
    park_id int not null,
    constraint fk_trail_park_id
		foreign key (park_id)
        references park(park_id)
);

create table organism (
    organism_id int primary key auto_increment,
    common_name varchar(250) not null,
    scientific_name varchar(250) not null,
    category varchar(100) not null,
    app_user_id int,
    constraint fk_organism_app_user_id
        foreign key (app_user_id)
        references app_user(app_user_id)
);

create table trail_review (
	trail_review_id int primary key auto_increment,
    rating decimal(4,2) not null,
    difficulty decimal(4,2) not null,
    comments text,
    trail_id int not null,
    app_user_id int,
    constraint fk_trail_review_app_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_trail_review_trail_id
		foreign key (trail_id)
        references trail(trail_id)
);

create table park_organism (
	park_organism_id int primary key auto_increment,
    abundance varchar(25) not null,
    nativeness varchar(25) not null,
    park_id int not null,
    organism_id int not null,
    app_user_id int,
    constraint fk_park_organism_app_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_park_organism_park_id
		foreign key (park_id)
        references park(park_id),
	constraint fk_park_organism_organism_id
		foreign key (organism_id)
        references organism(organism_id)
);

create table sighting (
	sighting_id int primary key auto_increment,
    `date` date not null,
    `time` time not null,
    comments text,
    latitude decimal(20,15) not null,
    longitude decimal(20,15) not null,
    organism_id int not null,
    app_user_id int,
    park_id int not null,
    constraint fk_sighting_app_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_sighting_organism_id
		foreign key (organism_id)
        references organism(organism_id),
    constraint fk_sighting_park_id
        foreign key (park_id)
        references park(park_id)
);

