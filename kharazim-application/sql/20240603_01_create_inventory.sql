create table `inventory`
(
    `id`                bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `clinic_code`       varchar(32) not null comment '诊所编码',
    `sku_code`          varchar(32) not null comment 'SKU编码',
    `quantity`          int         not null comment '数量',
    `occupied_quantity` int         not null comment '已预占数量',
    unique index udx_clinic_sku (`sku_code`, `clinic_code`),
    index idx_clinic_code (`clinic_code`)
) comment '商品库存';

create table `inventory_log`
(
    `id`                   bigint       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `source_business_code` varchar(32)  not null comment '关联业务编码',
    `sku_code`             varchar(32)  not null comment 'SKU编码',
    `quantity`             int unsigned not null comment '数量',
    `clinic_code`          varchar(32)  not null comment '诊所编码',
    `out_in_type`          int          not null comment '出入库类型',
    `operate_time`         datetime     not null,
    `operator`             varchar(64),
    `operator_code`        varchar(32),
    index idx_source_business_code (source_business_code)
) comment '库存流水日志';