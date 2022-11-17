create table wolfs
(
    id            integer primary key auto_increment,
    planned_order varchar(200) not null default '',
    eaten_people  varchar(200) not null default ''
)
