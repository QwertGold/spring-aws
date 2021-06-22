create table message
(
    id               text      not null,
    payload          text      not null,
    destination      text      not null,
    destination_type text      not null,
    attributes       text      not null,
    created_at       timestamp not null,
    next_send        timestamp not null,
    class            text      not null,
    status           text      not null,
    client_id        text      not null,
    primary key (id)
);
create index idx_message__next_send on message (next_send);
create index idx_message__created_at on message (status, created_at);