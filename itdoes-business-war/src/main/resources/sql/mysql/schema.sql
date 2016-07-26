drop table if exists ss_temp_invcompany;
drop table if exists ss_temp_part;

create table ss_temp_invcompany (
	invcompanyid bigint auto_increment,
	comment varchar(128) not null,
	partid bigint not null,
    primary key (invcompanyid)
) engine=InnoDB;

create table ss_temp_part (
	partid bigint auto_increment,
	barCode varchar(64) not null,
	registerdate timestamp not null default 0,
	primary key (partid)
) engine=InnoDB;
