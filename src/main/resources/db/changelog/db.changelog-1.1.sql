--liquibase formatted sql

--changeset milavitsky:1
create table users
  (
  id bigserial not null
  constraint user_pk primary key,
  username varchar (50) DEFAULT 'test',
  password varchar (128) DEFAULT '{noop}123',
  date_of_registration date,
  role varchar(15),
  is_deleted boolean
  );

--changeset milavitsky:2
  create unique index users_username_uindex
    on users (username);
