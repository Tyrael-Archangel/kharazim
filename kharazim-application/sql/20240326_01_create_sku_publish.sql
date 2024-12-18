create table `sku_publish`
(
    `id`           bigint         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`         varchar(32)    not null comment '商品发布序列号',
    `canceled`     bit            not null comment '是否已取消',
    `sku_code`     varchar(32)    not null comment 'SKU编码',
    `clinic_code`  varchar(32)    not null comment '诊所（机构）编码',
    `price`        decimal(10, 2) not null comment '单价（元）',
    `effect_begin` datetime       not null comment '生效时间',
    `effect_end`   datetime       not null comment '失效时间',
    `creator`      varchar(64),
    `creator_code` varchar(32),
    `create_time`  datetime,
    `updater`      varchar(64),
    `updater_code` varchar(32),
    `update_time`  datetime,
    `deleted`      bigint         not null default 0 comment '删除时间戳，0表示未删除',
    unique index udx_code (`code`),
    index idx_sku_code (`sku_code`),
    index idx_clinic_code (`clinic_code`),
    index idx_effect_begin (`effect_begin`),
    index idx_effect_end (`effect_end`)
) comment '商品发布';
