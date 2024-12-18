create table `supplier`
(
    `id`           bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`         varchar(32) not null comment '供应商编码',
    `name`         varchar(32) not null comment '供应商名称',
    `remark`       varchar(255) comment '备注',
    `creator`      varchar(64),
    `creator_code` varchar(32),
    `create_time`  datetime,
    `updater`      varchar(64),
    `updater_code` varchar(32),
    `update_time`  datetime,
    `deleted`      bigint      not null default 0 comment '删除时间戳，0表示未删除',
    unique index udx_code (`code`),
    unique index udx_name (`name`)
) comment '供应商';
