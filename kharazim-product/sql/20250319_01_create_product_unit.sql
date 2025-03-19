create table `product_unit`
(
    `id`           bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`         varchar(32) not null comment '单位编码',
    `name`         varchar(32) not null comment '单位名称',
    `english_name` varchar(64) comment '单位英文名称',
    `creator`      varchar(64),
    `creator_code` varchar(32),
    `create_time`  datetime,
    `updater`      varchar(64),
    `updater_code` varchar(32),
    `update_time`  datetime,
    `deleted`      bigint      not null default 0 comment '删除时间，0表示未删除',
    unique index udx_code (`code`),
    unique index udx_name (`name`, `deleted`)
) comment '商品单位';