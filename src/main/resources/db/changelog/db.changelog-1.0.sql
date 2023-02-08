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
    contractor_company varchar (30)
);

--changeset milavitsky:2
create table offers
  (
  id bigserial  not null
  constraint offers_pk primary key,
  company_bidder varchar (30) not null unique,
  offer numeric (10,2),
  offer_description varchar (100),
  answer boolean,
    id_tender bigserial not null
     constraint id_tender
     references tenders
  );

