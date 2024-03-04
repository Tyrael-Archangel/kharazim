create table `product_spu`
(
    `id`            bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`          varchar(32) not null comment 'SPU编码',
    `name`          varchar(64) not null comment 'SPU名称',
    `category_code` varchar(32) not null comment '商品分类编码',
    `supplier_code` varchar(32) not null comment '供应商编码',
    `default_image` varchar(512) comment '默认图片',
    `description`   text comment 'SPU描述信息',
    `creator`       varchar(64),
    `creator_code`  varchar(32),
    `create_time`   datetime,
    `updater`       varchar(64),
    `updater_code`  varchar(32),
    `update_time`   datetime,
    `deleted`       bit         not null default 0,
    unique index udx_code (`code`)
) comment 'SPU';
