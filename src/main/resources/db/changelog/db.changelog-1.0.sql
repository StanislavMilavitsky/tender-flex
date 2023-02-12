--liquibase formatted sql

--changeset milavitsky:1
create table cpv_codes
(
    cpv_code varchar (10)
     constraint cpv_code_pk primary key,
    cpv_description varchar (20)

);

--changeset milavitsky:2
create table tenders
(
    id bigserial not null
        constraint tender_pk primary key,
    official_name varchar (50) not null,
    national_registration_number varchar(20),
    country varchar (20),
    city varchar (50),
    name varchar (50) not null,
    surname varchar (50) not null,
    phone_number varchar (20),
    id_cpv_code varchar (10),
    type_of_tender varchar (20),
    description_of_the_procurement varchar (250),
    minimum_tender_value bigserial,
    maximum_tender_value bigserial,
    currency varchar (3),
    publication_date date,
    deadline_for_offer_submission date,
    deadline_for_signing_contract_submission date,
    contract varchar (30),
    award_decision varchar (30),
    reject_decision varchar (30),
    status varchar (10)
);

--changeset milavitsky:3
create table offers
(
    id bigserial not null
        constraint offer_pk primary key,
    id_tender bigserial
        constraint id_tender
        references tenders,
    official_name varchar (50) not null,
    national_registration_number varchar(20),
    country varchar (20),
    city varchar (50),
    name varchar (50) not null,
    surname varchar (50) not null,
    phone_number varchar (20),
    bid_price bigint,
    currency varchar (3),
    document varchar (30),
    status varchar (20),
    send_date date
);

