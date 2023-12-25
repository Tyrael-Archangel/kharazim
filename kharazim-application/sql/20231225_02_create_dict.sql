create table `dict`
(
    `id`                bigint      not null auto_increment primary key,
    `code`              varchar(64) not null,
    `name`              varchar(64) not null,
    `enabled`           bit         not null default 1,
    `remark`            varchar(255),
    `system_dict`       bit         not null default 0 comment '是否为系统字典',
    `allow_modify_item` bit         not null default 1 comment '是否允许修改字典项',
    `creator`           varchar(64) not null default '超级管理员',
    `creator_code`      varchar(32) not null default '000000',
    `create_time`       datetime    not null default current_timestamp,
    `updater`           varchar(64) not null default '超级管理员',
    `updater_code`      varchar(32) not null default '000000',
    `update_time`       datetime    not null default current_timestamp,
    unique index udx_code (`code`)
) comment '字典';

create table `dict_item`
(
    `id`        bigint      not null auto_increment primary key,
    `name`      varchar(64) not null,
    `value`     varchar(64) not null,
    `dict_code` varchar(64) not null,
    `sort`      int                  default 0,
    `enabled`   bit         not null default 1,
    `remark`    varchar(255),
    unique index udx_dict_code_value (`dict_code`, `value`)
) comment '字典项';
