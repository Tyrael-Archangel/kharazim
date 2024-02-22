create table `supplier`
(
    `id`           bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`         varchar(32) not null comment '单位编码',
    `name`         varchar(32) not null comment '单位名称',
    `remark`       varchar(255) comment '备注',
    `creator`      varchar(64),
    `creator_code` varchar(32),
    `create_time`  datetime,
    `updater`      varchar(64),
    `updater_code` varchar(32),
    `update_time`  datetime,
    `deleted`      bit         not null default 0,
    unique index udx_code (`code`)
) comment '供应商';
