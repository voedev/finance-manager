--liquibase formatted sql

--changeset admin:create-table_transaction_type
create type transaction_type_list as enum ('INCOME', 'EXPENSE');
create table public.transaction_type
(
    id   serial primary key,
    type transaction_type_list not null
);
--rollback drop table public.transaction_type;
--rollback drop type transaction_type_list;


--changeset admin:create-table_icon_pack_category
create type icon_pack_category_list as enum ('ICON_INCOME', 'ICON_EXPENSE', 'ICON_ACCOUNT');
create table public.icon_pack_category
(
    id   serial primary key,
    name icon_pack_category_list not null
);
--rollback drop table public.icon_pack_category
--rollback drop type icon_pack_category_list;


--changeset admin:create-table_icon_pack
create table public.icon_pack
(
    id   serial primary key,
    icon_name varchar(255) not null unique,
    path text not null
);
--rollback drop table public.icon_pack;


--changeset admin:create-table_icon_pack_icon_pack_category
create table public.icon_pack_icon_pack_category
(
    id serial primary key,
    icon_pack_id int not null,
    icon_pack_category_id int not null,

    foreign key (icon_pack_id) references public.icon_pack(id) on delete cascade,
    foreign key (icon_pack_category_id) references public.icon_pack_category(id) on delete cascade
);
--rollback drop table public.icon_pack_icon_pack_category;


--changeset admin:create-table_currency
create table public.currency
(
    id   serial primary key,
    value varchar(10) not null unique
);
--rollback drop table public.currency;


--changeset admin:create-table_users
create type users_status_list as enum ('ACTIVE', 'BLOCKED', 'DELETED', 'NOT_ACTIVATED');
create table public.users
(
    id   serial primary key,
    email varchar(255) not null unique,
    currency_id int not null,
    status users_status_list default 'NOT_ACTIVATED',
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,

    foreign key (currency_id) references public.currency(id)
);
--rollback drop table public.users;


--changeset admin:create-table_preset_category
create table public.preset_category
(
    id   serial primary key,
    icon_pack_id int default null,
    name varchar(255) not null unique,
    transaction_type_id int not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,

    foreign key (icon_pack_id) references public.icon_pack(id) on delete set null,
    foreign key (transaction_type_id) references public.transaction_type(id)
);
create index idx_preset_category_transaction_type on public.preset_category(transaction_type_id);
--rollback drop index idx_preset_category_transaction_type;
--rollback drop table public.preset_category;


--changeset admin:create-table_user_category
create table public.user_category
(
    id   serial primary key,
    user_id int not null,
    icon_pack_id int default null,
    name varchar(255) not null,
    transaction_type_id int not null,
    preset_category_id int default null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,

    foreign key (user_id) references public.users(id) on delete cascade,
    foreign key (icon_pack_id) references public.icon_pack(id) on delete set null,
    foreign key (transaction_type_id) references public.transaction_type(id),
    foreign key (preset_category_id) references public.preset_category(id),

    unique (name, user_id)
);
create index idx_user_category_user_id on public.user_category(user_id);
--rollback drop index idx_user_category_user_id;
--rollback drop table public.user_category;


--changeset admin:create-table_account
create type account_status_list as enum ('ACTIVE', 'DELETED');
create table public.account
(
    id   serial primary key,
    name varchar(255) not null,
    balance numeric(15, 2) default 0.00,
    currency_id int not null,
    user_id int not null,
    icon_pack_id int default null,
    status account_status_list default 'ACTIVE',
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,

    foreign key (currency_id) references public.currency(id),
    foreign key (user_id) references public.users(id) on delete cascade,
    foreign key (icon_pack_id) references public.icon_pack(id) on delete set null
);
--rollback drop table public.account;
--rollback drop type account_status_list;


--changeset admin:create-table_transaction
create table public.transaction
(
    id serial primary key,
    account_id int not null,
    user_category_id int not null,
    amount numeric(15, 2) not null,
    description text,
    transaction_date timestamp default current_timestamp,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,

    foreign key (account_id) references public.account(id) on delete cascade,
    foreign key (user_category_id) references public.user_category(id)
);
create index idx_transaction_account_id on public.transaction(account_id);
create index idx_transaction_category_id on public.transaction(user_category_id);
--rollback drop index idx_transaction_account_id;
--rollback drop index idx_transaction_category_id;
--rollback drop table public.transaction
