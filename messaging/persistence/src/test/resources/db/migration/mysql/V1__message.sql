create table message
(
    id               varchar(80)  not null,
    payload          longtext     not null,
    destination      varchar(255) not null,
    destination_type varchar(255) not null,
    attributes       longtext     not null,
    created_at       datetime(6)  not null,
    next_send        datetime(6)  not null,
    class            varchar(255) not null,
    status           varchar(80)  not null,
    client_id        varchar(80)  not null,
    primary key (id)
)
    character set utf8mb4
    collate utf8mb4_unicode_ci
;
create index idx_message__next_send on message (next_send);
create index idx_message__created_at on message (status, created_at);