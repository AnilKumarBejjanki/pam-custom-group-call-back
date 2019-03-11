select * from ATTACHMENT;
select * from CASEIDINFO;
select * from CASEROLEASSIGNMENTLOG;
select * from PROCESSINSTANCEINFO;
select * from processinstancelog;
select * from task where PROCESSINSTANCEID=86
select * from taskvariableimpl;
select * from processinstanceinfo
select * from variableinstancelog;
create table sample (id varchar2(20));
commit;

select * from sample;

insert into sample (id) values (1);
commit

create table users (username varchar(50),passwd varchar(50))
create table userroles (username varchar(50),userroles varchar(250),rolegroup varchar(250))
commit
insert into users(username,passwd) values ('kumar','kumar@123')
insert into users(username,passwd) values ('kieserver','kieserver1!')
insert into users(username,passwd) values ('controllerUser','controllerUser1!')
insert into users(username,passwd) values ('Administrator','Administrator@123')
controllerUser
select * from users;
select * from userroles;
describe users;
describe userroles

update users  set passwd='controllerUser1234' where username = 'controllerUser';
update userroles set userroles='admin' where username = 'controllerUser';
update userroles set userroles='admin' where username = 'anil';
update userroles set rolegroup='' where username = 'controllerUser' and userroles='admin';

insert into userroles (username,userroles,rolegroup) values ('aswanth','admin,kie-server,rest-all,user,Administrators,manager,process-admin','admin,kie-server,rest-all,user,Administrators,manager,process-admin')
insert into userroles (username,userroles,rolegroup) values ('controllerUser','admin,kie-server,rest-all,user,Administrators,manager,process-admin','admin,kie-server,rest-all,user,Administrators,manager,process-admin')
insert into userroles (username,userroles) values ('anil','admin,kie-server,rest-all,user,Administrators,manager,process-admin')
insert into userroles (username,userroles) values ('Administrator','Administrators')


insert into userroles (username,userroles) values ('kumar','developer')

insert into userroles (username,userroles) values ('aswanth','kie-server')
insert into userroles (username,userroles) values ('aswanth','rest-all')
insert into userroles (username,userroles) values ('aswanth','developer')

insert into userroles (username,userroles) values ('anil','kie-server')
insert into userroles (username,userroles) values ('anil','rest-all')


insert into userroles (username,userroles) values ('controllerUser','kie-server')
insert into userroles (username,userroles) values ('controllerUser','rest-all')

delete userroles where username = 'anil' and userroles='admin'
commit
select * from userroles
select * from users;