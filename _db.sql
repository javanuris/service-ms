//drop tables
drop table "Login"; drop table "Restore";
drop table "User"; drop table "Avatar"; drop table "Remember";
drop table "Activate";
drop table "Category"; drop table "Application"; drop table "Processing";
drop table "Photo"; drop table "Comment";

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
create table if not exists "Photo" (id identity primary key, name varchar(255), type varchar(255), file blob, modified datetime);
create table if not exists "Comment" (id identity primary key, user_id bigint references "User" (id), created timestamp, application_id bigint references "Application" (id), photo_id bigint references "Photo" (id), message varchar(255));

//init data
insert into "Login" (email, salt, password, attempt_left, status) values ('admin@test.com', '235ea634-28a6-422d-a565-aa7729878c88', '235ea634-28a6-422d-a565-aa7729878c88 5e14298c620c8294b5e9eb97452453c772d5199f2102a3d23d8df25f89595ff3', 5, 0);
insert into "Login" (email, salt, password, attempt_left, status) values ('user1@test.com', '235ea634-28a6-422d-a565-aa7729878c88', '235ea634-28a6-422d-a565-aa7729878c88 5e14298c620c8294b5e9eb97452453c772d5199f2102a3d23d8df25f89595ff3', 5, 0);
insert into "Login" (email, salt, password, attempt_left, status) values ('user2@test.com', '235ea634-28a6-422d-a565-aa7729878c88', '235ea634-28a6-422d-a565-aa7729878c88 5e14298c620c8294b5e9eb97452453c772d5199f2102a3d23d8df25f89595ff3', 5, 0);
insert into "Login" (email, salt, password, attempt_left, status) values ('user3@test.com', '235ea634-28a6-422d-a565-aa7729878c88', '235ea634-28a6-422d-a565-aa7729878c88 5e14298c620c8294b5e9eb97452453c772d5199f2102a3d23d8df25f89595ff3', 5, 0);
insert into "Login" (email, salt, password, attempt_left, status) values ('user4@test.com', '235ea634-28a6-422d-a565-aa7729878c88', '235ea634-28a6-422d-a565-aa7729878c88 5e14298c620c8294b5e9eb97452453c772d5199f2102a3d23d8df25f89595ff3', 5, 0);
insert into "Login" (email, salt, password, attempt_left, status) values ('user5@test.com', '235ea634-28a6-422d-a565-aa7729878c88', '235ea634-28a6-422d-a565-aa7729878c88 5e14298c620c8294b5e9eb97452453c772d5199f2102a3d23d8df25f89595ff3', 5, 0);
insert into "Login" (email, salt, password, attempt_left, status) values ('user6@test.com', '235ea634-28a6-422d-a565-aa7729878c88', '235ea634-28a6-422d-a565-aa7729878c88 5e14298c620c8294b5e9eb97452453c772d5199f2102a3d23d8df25f89595ff3', 5, 0);
insert into "Login" (email, salt, password, attempt_left, status) values ('user7@test.com', '235ea634-28a6-422d-a565-aa7729878c88', '235ea634-28a6-422d-a565-aa7729878c88 5e14298c620c8294b5e9eb97452453c772d5199f2102a3d23d8df25f89595ff3', 5, 0);
insert into "Login" (email, salt, password, attempt_left, status) values ('user8@test.com', '235ea634-28a6-422d-a565-aa7729878c88', '235ea634-28a6-422d-a565-aa7729878c88 5e14298c620c8294b5e9eb97452453c772d5199f2102a3d23d8df25f89595ff3', 5, 0);
insert into "Login" (email, salt, password, attempt_left, status) values ('user9@test.com', '235ea634-28a6-422d-a565-aa7729878c88', '235ea634-28a6-422d-a565-aa7729878c88 5e14298c620c8294b5e9eb97452453c772d5199f2102a3d23d8df25f89595ff3', 5, 0);
insert into "Login" (email, salt, password, attempt_left, status) values ('user0@test.com', '235ea634-28a6-422d-a565-aa7729878c88', '235ea634-28a6-422d-a565-aa7729878c88 5e14298c620c8294b5e9eb97452453c772d5199f2102a3d23d8df25f89595ff3', 5, 0);
insert into "Login" (email, salt, password, attempt_left, status) values ('manager@test.com', '235ea634-28a6-422d-a565-aa7729878c88', '235ea634-28a6-422d-a565-aa7729878c88 5e14298c620c8294b5e9eb97452453c772d5199f2102a3d23d8df25f89595ff3', 5, 0);
insert into "Login" (email, salt, password, attempt_left, status) values ('', '', '', 5, 0);
insert into "User" (first_name, login_id, role_name) values ('anonymous', select id from "Login" where email is '', 'anonymous');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Господь', '', 'Бог', select id from "Login" where email is 'admin@test.com', 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Вася', '', 'Пупкин', select id from "Login" where email is 'user1@test.com', 'authorized');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Хосе', '', 'Игнасио', select id from "Login" where email is 'user2@test.com', 'authorized');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Абай', '', 'Құнанбаев', select id from "Login" where email is 'user3@test.com', 'authorized');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Jessica', '', 'Alba', select id from "Login" where email is 'user4@test.com', 'authorized');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Jeniffer', '', 'Lopez', select id from "Login" where email is 'user5@test.com', 'authorized');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Владимир', '', 'Путин', select id from "Login" where email is 'user6@test.com', 'authorized');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Eva', '', 'Longoria', select id from "Login" where email is 'user7@test.com', 'authorized');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Bree', '', 'Van de Kamp', select id from "Login" where email is 'user8@test.com', 'authorized');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Marcello', '', 'Begetti', select id from "Login" where email is 'user9@test.com', 'authorized');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Mike', '', 'Tyson', select id from "Login" where email is 'user0@test.com', 'authorized');
insert into "User" (first_name, middle_name, last_name, login_id, role_name) values ('Евгения', '', '', select id from "Login" where email is 'manager@test.com', 'manager');
insert into "Category" (created, parent_id, name) values ('2016-10-01 12:00:00.00', null, 'Отопление / Heating');
insert into "Category" (created, parent_id, name) values ('2016-10-01 12:00:00.00', null, 'Водоснабжение / Water Supply');
insert into "Category" (created, parent_id, name) values ('2016-10-01 12:00:00.00', null, 'Уборка / Cleaning');
insert into "Category" (created, parent_id, name) values ('2016-10-01 12:00:00.00', 3, 'Уборка территории / Cleaning of territory');
insert into "Application" (created, user_id, category_id, message) values ('2016-10-01 13:00:00.000', '3', '1', 'Холодные батареи на кухне');
insert into "Application" (created, user_id, category_id, message) values ('2016-10-01 13:00:00.000', '4', '2', 'Нет горячей воды');
insert into "Application" (created, user_id, category_id, message) values ('2016-10-01 13:00:00.000', '5', '4', 'Мусор возле подъезда');
insert into "Comment" (created, user_id, application_id, message) values ('2016-10-01 14:00:00.000', '3', '1', 'Ждем ответа');
insert into "Comment" (created, user_id, application_id, message) values ('2016-10-01 14:00:00.000', '4', '2', 'Спасибо, уже проверяют');


//Select data


//scenario #1


//scenario #2