create table `settlement_order`
(
    `id`                       bigint         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`                     varchar(32)    not null comment '结算单编码',
    `customer_code`            varchar(32)    not null comment '会员编码',
    `clinic_code`              varchar(32)    not null comment '诊所（机构）编码',
    `total_amount`             decimal(10, 2) not null comment '总金额（元）',
    `source_prescription_code` varchar(32)    not null comment '来源处方编码',
    `status`                   int            not null comment '结算单状态',
    `settlement_time`          datetime comment '结算时间',
    `creator`                  varchar(64),
    `creator_code`             varchar(32),
    `create_time`              datetime,
    `updater`                  varchar(64),
    `updater_code`             varchar(32),
    `update_time`              datetime,
    `deleted`                  bigint         not null default 0 comment '删除时间戳，0表示未删除',
    unique index udx_code (`code`),
    index idx_customer_code (`customer_code`),
    index idx_clinic_code (`clinic_code`)
) comment '结算单';

create table `settlement_order_item`
(
    `id`                    bigint         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `settlement_order_code` varchar(32)    not null comment '结算单编码',
    `sku_code`              varchar(32)    not null comment 'SKU编码',
    `quantity`              int            not null comment '数量',
    `price`                 decimal(10, 2) not null comment '单价（元）',
    `amount`                decimal(10, 2) not null comment '商品项金额（元）',
    index idx_settlement_order_code (`settlement_order_code`)
) comment '结算单商品项';
