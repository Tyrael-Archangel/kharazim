create table `sku_publish`
(
    `id`           bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`         varchar(32) not null comment '商品发布序列号',
#     TODO @Tyrael Archangel
    `creator`      varchar(64),
    `creator_code` varchar(32),
    `create_time`  datetime,
    `updater`      varchar(64),
    `updater_code` varchar(32),
    `update_time`  datetime,
    `deleted`      bit         not null default 0,
    unique index udx_code (`code`)
) comment '商品发布';
