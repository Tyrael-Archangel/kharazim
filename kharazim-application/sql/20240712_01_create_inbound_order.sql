create table `inbound_order`
(
    `id`                         bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`                       varchar(32) not null comment '入库单号',
    `source_purchase_order_code` varchar(32) not null comment '采购单号',
    `clinic_code`                varchar(32) not null comment '诊所编码',
    `supplier_code`              varchar(32) not null comment '供应商编码',
    `status`                     int         not null comment '状态',
    `source_purchase_remark`     varchar(256) comment '来源采购备注',
    `creator`                    varchar(64),
    `creator_code`               varchar(32),
    `create_time`                datetime,
    `updater`                    varchar(64),
    `updater_code`               varchar(32),
    `update_time`                datetime,
    `deleted`                    bit         not null default 0,
    unique index udx_code (`code`),
    unique index udx_purchase_order_code (`source_purchase_order_code`),
    index idx_clinic_code (`clinic_code`),
    index idx_supplier_code (`supplier_code`)
) comment '入库单';

create table `inbound_order_item`
(
    `id`                 bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `inbound_order_code` varchar(32) not null comment '入库单号',
    `sku_code`           varchar(32) not null comment 'SKU编码',
    `quantity`           int         not null comment '数量',
    `received_quantity`  int         not null comment '已收货数量',
    index idx_inbound_order_code (inbound_order_code)
) comment '入库单商品明细';
