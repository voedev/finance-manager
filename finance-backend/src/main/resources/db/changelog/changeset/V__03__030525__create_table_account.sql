--liquibase formatted sql

--changeset admin:create-table_account
create table public.account
(
    id   bigserial primary key,
    title varchar(255) not null,
    balance numeric(15, 2) default 0.00,
    user_id bigint not null,
    currency_id bigint not null,
    status varchar(20) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,

    foreign key (user_id) references public.users(id) on delete cascade,
    foreign key (currency_id) references public.currency(id) on delete cascade
);
--rollback drop table public.account;