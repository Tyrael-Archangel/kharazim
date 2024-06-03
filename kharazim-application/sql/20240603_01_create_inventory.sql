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
