use finding_wildlife_test;

create table park (
	park_id int primary key auto_increment,
    park_name varchar(250) not null,
    center_latitude decimal(20,15) not null,
    center_longitude decimal(20,15) not null
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

delimiter //
create procedure set_known_good_state()
begin

         delete from sighting;
         alter table sighting auto_increment = 1;
         delete from park_organism;
         alter table park_organism auto_increment = 1;
         delete from trail_review;
         alter table trail_review auto_increment = 1;
         delete from trail;
         alter table trail auto_increment = 1;
         delete from park;
         alter table park auto_increment = 1;
         delete from organism;
         alter table organism auto_increment = 1;

        insert into
            organism (organism_id, common_name, scientific_name, category)
        values
            (1,"American Black Bear", "Ursus americanus", "mammal"),
            (2, "Bald eagle", "Haliaetus Ieucocephalus", "bird"),
            (3, "White-tailed deer", "Odocoileus virginianus", "mammal");

        insert into
            park (park_id, park_name, center_latitude, center_longitude)
        values
            (1, "Yellowstone National Park", 44.4280, -110.5885),
            (2, "White Sands National Park", 32.7872, -106.3257),
            (3, "Joshua Tree National Park", 33.8734, -115.9010);

        insert into
            trail (trail_id, trail_name, trail_number, trail_distance, park_id)
        values
            (1, 'YS one', 10, 5, 1),
            (2, 'YS two', 11, 30, 1),
            (3, 'WS one', 1, 4, 2),
            (4, 'JT one', 5, 24, 3);

        insert into
            trail_review (trail_review_id, rating, difficulty, comments, trail_id, app_user_id)
        values
            (1, 3.50, 5.00, '', 1, 1),
            (2, 5.00, 1.00, 'this trail is fun for beginners', 2, 2),
            (3, 2.30, 5.00, '', 3, 2);

        insert into
            park_organism (park_organism_id, abundance, nativeness, park_id, organism_id)
        values
            (1, 'Occasional', 'Native', 1, 1),
            (2, 'Common', 'Native', 2, 1),
            (3, 'Uncommon', 'Non-native', 3, 3);

        insert into
            sighting (sighting_id, `date`, `time`, comments, latitude, longitude, park_id, organism_id, app_user_id)
        values
            (1, '2020-05-01', '13:45:00', 'Yellowstone was beautiful', 44.5250489, -110.83819, 1, 1, 1),
            (2, '2021-08-05', '08:45:00', 'White Sands is one of my favorite national parks to visit.', 32.8295, -106.2731, 2, 3, 2),
            (3, '2021-06-03', '07:00:00', 'Joshua Tree is a must visit when you are in southern Cali', 33.7342, -115.7992, 3, 3, 2);


end //
delimiter ;