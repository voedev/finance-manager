--liquibase formatted sql

--changeset admin:create-refresh_token
create table public.refresh_token
(
    id bigserial primary key,
    user_id bigint not null,
    token varchar(128),
    expiry_date timestamp,
    revoked boolean default false,
    created_at timestamp default current_timestamp,

    foreign key (user_id) references public.users(id) on delete cascade
);
--rollback drop table public.refresh_token;