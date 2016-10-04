//drop tables
drop table "Login"; drop table "Restore";
drop table "User"; drop table "Avatar"; drop table "Remember";
drop table "Activate";
drop table "Category"; drop table "Application"; drop table "Processing";
drop table "Attachment"; drop table "Comment";

//create tables
create table if not exists "Login" (id identity primary key, email varchar(255) unique not null, salt varchar(255) not null, password varchar(255) not null, attempt_left int not null, status int not null);
create table if not exists "Restore" (id identity primary key, login_id bigint references "Login" (id), code varchar(255), cookie_name varchar(255), cookie_value varchar(255), valid datetime);
create table if not exists "Avatar" (id identity primary key, name varchar(255), type varchar(255), file blob, modified datetime);
create table if not exists "User" (id identity primary key, first_name varchar(255), middle_name varchar(255), last_name varchar(255), login_id bigint references "Login" (id), role_name varchar(255), avatar_id bigint references "Avatar" (id));
create table if not exists "Remember" (id identity primary key, user_id bigint references "User" (id), cookie_name varchar(255), cookie_value varchar(255), valid datetime);
create table if not exists "Activate" (id identity primary key, email varchar(255) unique, salt varchar(128) not null, password varchar(255), code varchar(255), valid datetime);
create table if not exists "Category" (id identity primary key, created timestamp, parent_id bigint references "Category" (id), name varchar(255) not null);
create table if not exists "Application" (id identity primary key, created timestamp, user_id bigint references "User" (id), category_id bigint references "Category" (id), message varchar(255) not null);
create table if not exists "Processing" (id identity primary key, application_id bigint references "Application" (id), user_id bigint references "User" (id), created timestamp);
create table if not exists "Attachment" (id identity primary key, name varchar(255), type varchar(255), file blob, created datetime);
create table if not exists "Comment" (id identity primary key, user_id bigint references "User" (id), created timestamp, application_id bigint references "Application" (id), attachment_id bigint references "Attachment" (id), message varchar(255));

//init data
insert into "Login" (email, salt, password, attempt_left, status) values ('test@test.com', 'salt', 'test', 5, 0);
insert into "Login" (email, salt, password, attempt_left, status) values ('', '', '', 5, 0);
insert into "User" (first_name, login_id, role_name) values ('anonymous', select id from "Login" where email is '', 'anonymous');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('John', 'M.', 'Dow', select id from "Login" where email is 'test@test.com', 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Peter', 'A.', 'Hoff', select id from "Login" where email is 'test@test.com', 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Joseph', 'K.', 'Lemon', select id from "Login" where email is 'test@test.com', 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Jason', 'M.', 'Bourne', select id from "Login" where email is 'test@test.com', 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Michael', 'F.', 'Kane', select id from "Login" where email is 'test@test.com', 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Rebecca', 'W.', 'Douglas', select id from "Login" where email is 'test@test.com', 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Boris', 'M.', 'Staff', select id from "Login" where email is 'test@test.com', 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Dilan', 'R.', 'Peterson', select id from "Login" where email is 'test@test.com', 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Masha', 'O.', 'Brian', select id from "Login" where email is 'test@test.com', 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Marcello', 'P.', 'Begetti', select id from "Login" where email is 'test@test.com', 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Joshua', 'B.', 'Scava', select id from "Login" where email is 'test@test.com', 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Edie', 'N.', 'White', select id from "Login" where email is 'test@test.com', 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Gregory', 'Z.', 'Shvartz', select id from "Login" where email is 'test@test.com', 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Bill', 'L.', 'Black', select id from "Login" where email is 'test@test.com', 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Latisha', 'P.', 'Morrisson', select id from "Login" where email is 'test@test.com', 'admin');
insert into "Category" (created, parent_id, name) values ('2016-10-11 17:36:17.528', null, 'Heating');


//Select data


//scenario #1


//scenario #2