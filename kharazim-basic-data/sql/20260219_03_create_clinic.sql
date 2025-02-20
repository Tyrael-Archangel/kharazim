create table `clinic`
(
    `id`           bigint       not null auto_increment primary key,
    `code`         varchar(32)  not null comment '诊所（机构）编码',
    `name`         varchar(128) not null comment '诊所（机构）名称',
    `english_name` varchar(128) comment '诊所（机构）英文名称',
    `image`        varchar(512) comment '图片',
    `status`       int          not null comment '状态',
    `creator`      varchar(64),
    `creator_code` varchar(32),
    `create_time`  datetime,
    `updater`      varchar(64),
    `updater_code` varchar(32),
    `update_time`  datetime,
    `deleted`      bigint       not null default 0 comment '删除时间，0表示未删除',
    unique index udx_code (`code`),
    unique index udx_name (`name`, `deleted`)
) comment '诊所（机构）';

