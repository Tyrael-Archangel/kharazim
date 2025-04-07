create table `inbound_order`
(
    `id`                   bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`                 varchar(32) not null comment '入库单号',
    `source_business_code` varchar(32) not null comment '来源单据编码',
    `clinic_code`          varchar(32) not null comment '诊所编码',
    `supplier_code`        varchar(32) not null comment '供应商编码',
    `status`               int         not null comment '状态',
    `source_remark`        varchar(256) comment '来源备注',
    `source_type`          int         not null comment '来源类型',
    `creator`              varchar(64),
    `creator_code`         varchar(32),
    `create_time`          datetime,
    `updater`              varchar(64),
    `updater_code`         varchar(32),
    `update_time`          datetime,
    `deleted`              bigint      not null default 0 comment '删除时间，0表示未删除',
    unique index udx_code (`code`),
    unique index udx_source_business_code (`source_business_code`, `deleted`),
    index idx_clinic_code (`clinic_code`),
    index idx_supplier_code (`supplier_code`)
) comment '入库单';

create table `inbound_order_item`
(
    `id`                 bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `inbound_order_code` varchar(32) not null comment '入库单号',
    `sku_code`           varchar(32) not null comment 'SKU编码',
    `quantity`           int         not null comment '数量',
    `inbounded_quantity` int         not null comment '已入库数量',
    index idx_inbound_order_code (inbound_order_code)
) comment '入库单商品明细';
