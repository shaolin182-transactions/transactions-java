CREATE extension if not exists "pgcrypto";

create table bank_accounts (
   id integer not null,
   category varchar(255),
   label varchar(255),
   primary key (id)
);

create table categories (
    id integer not null,
    category varchar(255),
    label varchar(255),
    type varchar(255),
    primary key (id)
);

create table transaction_details (
    bank_account_id integer not null,
    category_id integer,
    income float4,
    outcome float4,
    cost bigint,
    cost_abs bigint,
    id uuid not null,
    transaction_id uuid,
    description varchar(255),
    primary key (id)
);

create table transactions (
    cost bigint,
    cost_abs bigint,
    date timestamp(6) with time zone,
    id uuid not null,
    description varchar(255),
    type varchar(255),
    primary key (id)
);

alter table transaction_details alter column id set default gen_random_uuid();

alter table if exists transaction_details
    add constraint fk_tr_details_bk
    foreign key (bank_account_id)
    references bank_accounts;

alter table if exists transaction_details
    add constraint fk_tr_details_category
    foreign key (category_id)
    references categories;

alter table if exists transaction_details
    add constraint fk_tr_details_transaction
    foreign key (transaction_id)
    references transactions;