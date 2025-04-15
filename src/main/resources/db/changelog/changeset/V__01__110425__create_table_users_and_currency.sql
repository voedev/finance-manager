--liquibase formatted sql

--changeset admin:create-table_currency
create table public.currency
(
    id   serial primary key,
    value varchar(3) not null unique
);
--rollback drop table public.currency;


--changeset admin:create-table_users
create type users_status_list as enum ('ACTIVE', 'BLOCKED', 'DELETED', 'NOT_ACTIVATED');
create type users_role_list as enum ('ROLE_ADMIN', 'ROLE_USER');

create table public.users
(
    id serial primary key,
    email varchar(255) not null unique,
    password varchar(255) not null,
    currency_id int default null,
    status users_status_list default 'NOT_ACTIVATED',
    role users_role_list default 'ROLE_USER',
    verification_code varchar(100),
    verification_expiry timestamp,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,

    foreign key (currency_id) references public.currency(id)
);
--rollback drop table public.users;
--rollback drop type users_status_list;
--rollback drop type users_role_list;