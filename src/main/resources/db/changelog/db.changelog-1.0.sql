--liquibase formatted sql

--changeset milavitsky:1
create table tenders
(
    id bigserial not null
   constraint tenders_pk primary key,
    title varchar(20) not null,
    tender_description varchar(100),
    budget numeric (10,2),
    date_of_start date,
    date_of_end date,
    is_deleted boolean,
    user_company varchar (30)
);

