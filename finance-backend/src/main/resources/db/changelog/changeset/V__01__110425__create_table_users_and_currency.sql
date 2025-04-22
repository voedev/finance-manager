--liquibase formatted sql

--changeset admin:create-table_currency
create table public.currency
(
    id bigserial primary key,
    value varchar(3) not null unique
);
--rollback drop table public.currency;


--changeset admin:create-table_users
create table public.users
(
    id bigserial primary key,
    email varchar(255) not null unique,
    password varchar(255) not null,
    currency_id bigint default null,
    status varchar(20) not null,
    role varchar(20) not null,
    verification_code varchar(128),
    verification_expiry timestamp,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,

    foreign key (currency_id) references public.currency(id)
);
--rollback drop table public.users;
