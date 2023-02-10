--liquibase formatted sql

--changeset milavitsky:1
create table contact_persons
(
    id bigserial  not null
        constraint contact_person_pk primary key,
    name varchar (50) not null,
    surname varchar (50) not null,
    phone_number bigserial
);

--changeset milavitsky:2
create table subject_matter_of_the_procurements
(
    id bigserial  not null
        constraint subject_matter_of_the_procurement_pk primary key,
    cpv_code bigint,
    type_of_tender varchar (20),
    description_of_the_procurement varchar (250),
    minimum_tender_value bigserial,
    maximum_tender_value bigserial,
    currency varchar (3)
);

--changeset milavitsky:3
create table dates
(
    id bigserial  not null
        constraint date_pk primary key,
    publication_date date,
    deadline_for_offer_submission date,
    deadline_for_signing_contract_submission date
);

--changeset milavitsky:4
create table documents
(
    id bigserial  not null
        constraint document_pk primary key,
    contract varchar (20),
    award_decision varchar (20),
    reject_decision varchar (20)
);

--changeset milavitsky:5
create table tenders
(
    id bigserial not null
        constraint tenders_pk primary key,
    official_name varchar(50) not null,
    national_registration_number varchar(20),
    country varchar (20),
    city varchar (50),
    tender_description varchar(100),
    id_contact_person bigserial
    constraint id_contact_person
        references contact_persons,
    id_subject_matter_of_the_procurement bigserial
        constraint id_subject_matter_of_the_procurement
        references subject_matter_of_the_procurements,
    id_date bigserial
        constraint id_date
        references dates,
    id_document bigserial
        constraint id_document
            references documents,
    icon varchar (20)


);

