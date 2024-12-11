create table `dict_item`
(
    `id`        bigint      not null auto_increment primary key,
    `dict_code` varchar(64) not null,
    `key`       varchar(64) not null,
    `value`     varchar(64) not null,
    `sort`      int default 0,
    unique index udx_dict_code_value (`dict_code`, `key`)
) comment '字典项';
