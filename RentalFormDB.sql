drop database if exists RentalFormDB;

create database RentalFormDB;

use RentalFormDB;

create table `user` (
	id int primary key auto_increment,
    username varchar(30) not null,
    `password` varchar(100) not null,
    enabled boolean not null
);

create table `role` (
	id int primary key auto_increment,
    `role` varchar(30)
);

create table `user_role` (
	`user_id` int not null,
    `role_id` int not null,
    primary key(`user_id`, `role_id`),
    foreign key(`user_id`) references `user`(id),
    foreign key(`role_id`) references `role`(id)
);

insert into `user`(`username`,`password`,`enabled`)	VALUES
					("admin", "$2a$10$6/krDDN8P/i/CANT5Kn2pO6FSkczr7QbNpaaz/wnMYUpFoy/.vy9C", 1),
					("cference", "$2a$10$6/krDDN8P/i/CANT5Kn2pO6FSkczr7QbNpaaz/wnMYUpFoy/.vy9C", 1);
                    
insert into `role` (`role`) VALUES
				('ROLE_ADMIN'),
                ('ROLE_USER');
                
insert into `user_role` (`user_id`, `role_id`) VALUES
						(1,1),
                        (1,2),
                        (2,2);
					
        