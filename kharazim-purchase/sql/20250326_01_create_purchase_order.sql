create table `purchase_order`
(
    `id`             bigint         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`           varchar(32)    not null comment '采购单号',
    `clinic_code`    varchar(32)    not null comment '诊所编码',
    `supplier_code`  varchar(32)    not null comment '供应商编码',
    `receive_status` int            not null comment '收货状态',
    `payment_status` int            not null comment '结算状态',
    `total_amount`   decimal(10, 2) not null comment '总金额（元）',
    `paid_amount`    decimal(10, 2) not null comment '已结算金额（元）',
    `remark`         varchar(256) comment '备注',
    `creator`        varchar(64),
    `creator_code`   varchar(32),
    `create_time`    datetime,
    `updater`        varchar(64),
    `updater_code`   varchar(32),
    `update_time`    datetime,
    `deleted`        bigint         not null default 0 comment '删除时间，0表示未删除',
    unique index udx_code (`code`),
    index idx_clinic_code (`clinic_code`),
    index idx_supplier_code (`supplier_code`)
) comment '采购单';

create table `purchase_order_item`
(
    `id`                  bigint         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `purchase_order_code` varchar(32)    not null comment '采购单号',
    `sku_code`            varchar(32)    not null comment 'SKU编码',
    `quantity`            int            not null comment '数量',
    `received_quantity`   int            not null comment '已收货数量',
    `price`               decimal(10, 2) not null comment '单价（元）',
    `amount`              decimal(10, 2) not null comment '商品项金额（元）',
    index idx_purchase_order_code (`purchase_order_code`)
) comment '采购单商品明细';

create table `purchase_order_payment_record`
(
    `id`                  bigint         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `serial_code`         varchar(32)    not null comment '流水号',
    `purchase_order_code` varchar(32)    not null comment '采购单号',
    `amount`              decimal(10, 2) not null comment '商品项金额（元）',
    `payment_time`        datetime       not null comment '支付时间',
    `payment_user`        varchar(64)    not null comment '支付用户',
    `payment_user_code`   varchar(32)    not null comment '支付用户编码',
    `vouchers`            json comment '支付凭证',
    unique index udx_serial_code (`serial_code`),
    index idx_purchase_order_code (`purchase_order_code`)
) comment '采购单支付记录';

create table `purchase_order_receive_record`
(
    `id`                  bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `serial_code`         varchar(32) not null comment '流水号',
    `purchase_order_code` varchar(32) not null comment '采购单号',
    `tracking_number`     varchar(64) not null comment '物流跟踪号',
    `receive_time`        datetime    not null comment '收货时间',
    `receive_user`        varchar(64) not null comment '收货人',
    `receive_user_code`   varchar(32) not null comment '收货人编码',
    unique index udx_serial_code (`serial_code`),
    index idx_purchase_order_code (`purchase_order_code`)
) comment '采购单收货记录';

create table `purchase_order_receive_record_item`
(
    `id`                  bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `receive_serial_code` varchar(32) not null comment '收货记录流水号',
    `sku_code`            varchar(32) not null comment '收货商品编码',
    `quantity`            int         not null comment '收货数量',
    index idx_receive_serial_code (`receive_serial_code`)
) comment '采购单收货明细';
