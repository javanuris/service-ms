//drop tables
drop table "Permission"; drop table "Role"; drop table "RolePermission"; drop table "Login"; drop table "Avatar"; drop table "User";


//create tables
create table if not exists "Permission" (id identity primary key, uri varchar(255) unique);
create table if not exists "Role" (id identity primary key, name varchar(255) unique);
create table if not exists "RolePermission" (id identity primary key, role_id bigint references "Role" (id), permission_id bigint references "Permission" (id));
create table if not exists "Login" (id identity primary key, email varchar(255) unique, password varchar(255), attempt_left int, status int);
create table if not exists "Avatar" (id identity primary key, name varchar(255), type varchar(255), file blob);
create table if not exists "User" (id identity primary key, first_name varchar(255), middle_name varchar(255), last_name varchar(255), login_id bigint references "Login" (id), role_id bigint references "Role" (id), avatar_id bigint references "Avatar" (id));


//init data
delete from "Permission";
insert into "Permission" (uri) values ('/');
insert into "Permission" (uri) values ('/home');
insert into "Permission" (uri) values ('/develop');
insert into "Permission" (uri) values ('/profile/login');
insert into "Permission" (uri) values ('/profile/register');
insert into "Permission" (uri) values ('/profile/view');
insert into "Permission" (uri) values ('/profile/reset-password');
insert into "Permission" (uri) values ('/profile/edit');
insert into "Permission" (uri) values ('/profile/logout');
insert into "Permission" (uri) values ('/rbac/user/list');
insert into "Permission" (uri) values ('/rbac/user/view');
insert into "Permission" (uri) values ('/rbac/role/list');
insert into "Permission" (uri) values ('/rbac/permission/list');
insert into "Permission" (uri) values ('/application/list');
insert into "Permission" (uri) values ('/execution/list');
insert into "Permission" (uri) values ('/employee/list');
insert into "Permission" (uri) values ('/service/list');
delete from "Role";
insert into "Role" (name) values ('anonymous');
insert into "Role" (name) values ('admin');
delete from "RolePermission";
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'anonymous', select id from "Permission" where uri is '/');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'anonymous', select id from "Permission" where uri is '/home');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'anonymous', select id from "Permission" where uri is '/profile/login');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'anonymous', select id from "Permission" where uri is '/profile/register');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/home');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/develop');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/profile/view');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/profile/reset-password');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/profile/edit');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/profile/logout');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/rbac/user/list');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/rbac/user/view');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/rbac/role/list');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/rbac/permission/list');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/application/list');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/execution/list');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/employee/list');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/service/list');
insert into "Login" (email, password, attempt_left, status) values ('test@test.com', 'test', 5, 0);
insert into "User" (first_name, role_id) values ('anonymous', select id from "Role" where name is 'anonymous');
insert into "User" (first_name, middle_name, last_name, login_id, role_id) values ('John', 'M.', 'Dow', select id from "Login" where email is 'test@test.com', select id from "Role" where name is 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_id) values ('Peter', 'A.', 'Hoff', select id from "Login" where email is 'test@test.com', select id from "Role" where name is 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_id) values ('Joseph', 'K.', 'Lemon', select id from "Login" where email is 'test@test.com', select id from "Role" where name is 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_id) values ('Jason', 'M.', 'Bourne', select id from "Login" where email is 'test@test.com', select id from "Role" where name is 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_id) values ('Michael', 'F.', 'Kane', select id from "Login" where email is 'test@test.com', select id from "Role" where name is 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_id) values ('Rebecca', 'W.', 'Douglas', select id from "Login" where email is 'test@test.com', select id from "Role" where name is 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_id) values ('Boris', 'M.', 'Staff', select id from "Login" where email is 'test@test.com', select id from "Role" where name is 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_id) values ('Dilan', 'R.', 'Peterson', select id from "Login" where email is 'test@test.com', select id from "Role" where name is 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_id) values ('Masha', 'O.', 'Brian', select id from "Login" where email is 'test@test.com', select id from "Role" where name is 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_id) values ('Marcello', 'P.', 'Begetti', select id from "Login" where email is 'test@test.com', select id from "Role" where name is 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_id) values ('Joshua', 'B.', 'Scava', select id from "Login" where email is 'test@test.com', select id from "Role" where name is 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_id) values ('Edie', 'N.', 'White', select id from "Login" where email is 'test@test.com', select id from "Role" where name is 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_id) values ('Gregory', 'Z.', 'Shvartz', select id from "Login" where email is 'test@test.com', select id from "Role" where name is 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_id) values ('Bill', 'L.', 'Black', select id from "Login" where email is 'test@test.com', select id from "Role" where name is 'admin');
insert into "User" (first_name, middle_name, last_name, login_id, role_id) values ('Latisha', 'P.', 'Morrisson', select id from "Login" where email is 'test@test.com', select id from "Role" where name is 'admin');


//select data
select r.id, r.name, p.id, p.uri from "Role" as r join "Permission" as p, "RolePermission" as rp where r.id = rp.role_id and p.id = rp.permission_id;
select u.id, u.first_name, u.middle_name, u.last_name, r.id, r.name, p.id, p.uri from "User" as u join "Role" as r, "Permission" as p, "RolePermission" as rp where r.id = u.role_id and r.id=rp.role_id and p.id = rp.permission_id;
select u.id, u.first_name, u.middle_name, u.last_name, l.email, l.password from "User" as u join "Login" as l where u.login_id = l.id;
select u.id, u.first_name, u.middle_name, u.last_name, u.login_id from "User" as u where u.login_id is null;

//scenario #1


//scenario #2