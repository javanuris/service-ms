//drop tables
drop table "Permission"; drop table "Role"; drop table "RolePermission";
drop table "Login"; drop table "Restore";
drop table "User"; drop table "Avatar"; drop table "Remember";
drop table "Activate";


//create tables
create table if not exists "Permission" (id identity primary key, uri varchar(255) unique not null, action varchar(255) not null);
create table if not exists "Role" (id identity primary key, name varchar(255) unique not null);
create table if not exists "RolePermission" (id identity primary key, role_id bigint references "Role" (id), permission_id bigint references "Permission" (id));
create table if not exists "Login" (id identity primary key, email varchar(255) unique not null, salt varchar(255) not null, password varchar(255) not null, attempt_left int not null, status int not null);
create table if not exists "Restore" (id identity primary key, login_id bigint references "Login" (id), code varchar(255), cookie_name varchar(255), cookie_value varchar(255), valid datetime);
create table if not exists "Avatar" (id identity primary key, name varchar(255), type varchar(255), file blob, modified datetime);
create table if not exists "User" (id identity primary key, first_name varchar(255), middle_name varchar(255), last_name varchar(255), login_id bigint references "Login" (id), role_id bigint references "Role" (id), avatar_id bigint references "Avatar" (id));
create table if not exists "Remember" (id identity primary key, user_id bigint references "User" (id), cookie_name varchar(255), cookie_value varchar(255), valid datetime);
create table if not exists "Activate" (id identity primary key, email varchar(255) unique, salt varchar(128) not null, password varchar(255), code varchar(255), valid datetime);


//init data
delete from "Permission";
insert into "Permission" (uri, action) values ('/', 'Home');
insert into "Permission" (uri, action) values ('/home', 'Home');
insert into "Permission" (uri, action) values ('/develop', 'Develop');
insert into "Permission" (uri, action) values ('/profile/login', 'profile.Login');
insert into "Permission" (uri, action) values ('/profile/register', 'profile.Register');
insert into "Permission" (uri, action) values ('/profile/activate', 'profile.Activate');
insert into "Permission" (uri, action) values ('/profile/restore-password', 'profile.RestorePassword');
insert into "Permission" (uri, action) values ('/profile/reset-password', 'profile.ResetPassword');
insert into "Permission" (uri, action) values ('/profile/view', 'profile.View');
insert into "Permission" (uri, action) values ('/profile/edit', 'profile.Edit');
insert into "Permission" (uri, action) values ('/profile/logout', 'profile.Logout');
insert into "Permission" (uri, action) values ('/rbac/user/list', 'rbac.user.List');
insert into "Permission" (uri, action) values ('/rbac/user/view', 'rbac.user.View');
insert into "Permission" (uri, action) values ('/rbac/user/edit', 'rbac.user.Edit');
insert into "Permission" (uri, action) values ('/rbac/role/list', 'rbac.role.List');
insert into "Permission" (uri, action) values ('/rbac/role/view', 'rbac.role.View');
insert into "Permission" (uri, action) values ('/rbac/role/edit', 'rbac.role.Edit');
insert into "Permission" (uri, action) values ('/rbac/permission/list', 'rbac.permission.List');
insert into "Permission" (uri, action) values ('/application/list', 'application.List');
insert into "Permission" (uri, action) values ('/execution/list', 'execution.List');
insert into "Permission" (uri, action) values ('/employee/list', 'employee.List');
insert into "Permission" (uri, action) values ('/service/list', 'service.List');
delete from "Role";
insert into "Role" (name) values ('anonymous');
insert into "Role" (name) values ('authorized');
insert into "Role" (name) values ('admin');
delete from "RolePermission";
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'anonymous', select id from "Permission" where uri is '/');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'anonymous', select id from "Permission" where uri is '/home');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'anonymous', select id from "Permission" where uri is '/profile/login');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'anonymous', select id from "Permission" where uri is '/profile/register');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'anonymous', select id from "Permission" where uri is '/profile/activate');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'anonymous', select id from "Permission" where uri is '/profile/restore-password');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'authorized', select id from "Permission" where uri is '/');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'authorized', select id from "Permission" where uri is '/home');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'authorized', select id from "Permission" where uri is '/profile/view');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'authorized', select id from "Permission" where uri is '/profile/reset-password');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'authorized', select id from "Permission" where uri is '/profile/edit');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'authorized', select id from "Permission" where uri is '/profile/logout');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/home');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/develop');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/profile/view');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/profile/reset-password');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/profile/edit');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/profile/logout');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/rbac/user/list');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/rbac/user/view');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/rbac/user/edit');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/rbac/role/list');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/rbac/role/view');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/rbac/role/edit');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/rbac/permission/list');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/application/list');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/execution/list');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/employee/list');
insert into "RolePermission" (role_id, permission_id) values (select id from "Role" where name is 'admin', select id from "Permission" where uri is '/service/list');
insert into "Login" (email, salt, password, attempt_left, status) values ('test@test.com', 'salt', 'test', 5, 0);
insert into "Login" (email, salt, password, attempt_left, status) values ('', '', '', 5, 0);
insert into "User" (first_name, login_id, role_id) values ('anonymous', select id from "Login" where email is '', select id from "Role" where name is 'anonymous');
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


//Select data
select r.id, r.name, p.id, p.uri from "Role" as r join "Permission" as p, "RolePermission" as rp where r.id = rp.role_id and p.id = rp.permission_id;
select u.id, u.first_name, u.middle_name, u.last_name, r.id, r.name, p.id, p.uri from "User" as u join "Role" as r, "Permission" as p, "RolePermission" as rp where r.id = u.role_id and r.id=rp.role_id and p.id = rp.permission_id;
select u.id, u.first_name, u.middle_name, u.last_name, l.email, l.password from "User" as u join "Login" as l where u.login_id = l.id;
select u.id, u.first_name, u.middle_name, u.last_name, u.login_id from "User" as u where u.login_id is null;

//scenario #1


//scenario #2