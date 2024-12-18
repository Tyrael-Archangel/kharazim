create table `outbound_order`
(
    `id`                   bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`                 varchar(32) not null comment '出库单号',
    `status`               int         not null comment '状态',
    `source_business_code` varchar(32) not null comment '来源单据编码',
    `customer_code`        varchar(32) not null comment '会员编码',
    `clinic_code`          varchar(32) not null comment '诊所编码',
    `source_remark`        varchar(256) comment '来源备注',
    `creator`              varchar(64),
    `creator_code`         varchar(32),
    `create_time`          datetime,
    `updater`              varchar(64),
    `updater_code`         varchar(32),
    `update_time`          datetime,
    `deleted`              bigint      not null default 0 comment '删除时间戳，0表示未删除',
    unique index udx_code (`code`),
    unique index udx_source_business_code (`source_business_code`, `deleted`),
    index idx_clinic_code (`clinic_code`),
    index idx_customer_code (`customer_code`)
) comment '出库单';

create table `outbound_order_item`
(
    `id`                  bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `outbound_order_code` varchar(32) not null comment '入库单号',
    `sku_code`            varchar(32) not null comment 'SKU编码',
    `quantity`            int         not null comment '数量',
    index idx_outbound_order_code (outbound_order_code)
) comment '出库单商品明细';
