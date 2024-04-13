create table `file`
(
    `id`           varchar(32)  not null primary key,
    `name`         varchar(128) comment '文件名',
    `path`         varchar(512) not null comment '文件路径',
    `content_type` varchar(32),
    `creator`      varchar(64)  not null,
    `creator_code` varchar(32)  not null,
    `create_time`  datetime     not null
) comment '文件';
