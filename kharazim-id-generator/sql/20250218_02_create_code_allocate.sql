create table `code_allocate`
(
    `id`           bigint      not null primary key auto_increment,
    `tag`          varchar(64) not null,
    `next_value`   bigint      not null,
    `created_time` datetime    not null,
    unique index udx_tag (`tag`)
);
