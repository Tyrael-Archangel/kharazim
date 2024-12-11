create table `user`
(
    `id`                   bigint      not null auto_increment primary key,
    `code`                 varchar(32) not null,
    `name`                 varchar(64) not null,
    `nick_name`            varchar(64),
    `english_name`         varchar(64) comment '用户英文名',
    `avatar`               varchar(512) comment '头像',
    `gender`               int comment '性别，1-男性，2-女性',
    `birthday`             date comment '生日',
    `phone`                varchar(32) comment '手机号码',
    `password`             varchar(64) not null,
    `need_change_password` bit         not null default 0 comment '是否需要修改密码（新建的用户或者管理员重置密码后建议用户自己修改密码）',
    `status`               int         not null default 1 comment '状态，1-已启用，2-已禁用',
    `wechat_code`          varchar(64) comment '微信号',
    `wechat_union_id`      varchar(64) comment '微信union_id',
    `wechat_name`          varchar(64) comment '微信名',
    `certificate_type`     int comment '证件类型',
    `certificate_code`     varchar(64) comment '证件号码',
    `remark`               varchar(255) comment '备注',
    `creator`              varchar(64) not null,
    `creator_code`         varchar(32) not null,
    `create_time`          datetime    not null,
    `updater`              varchar(64) not null,
    `updater_code`         varchar(32) not null,
    `update_time`          datetime    not null,
    `deleted`              bit         not null default 0,
    `deleted_timestamp`    bigint      not null default 0 comment '删除时间戳',
    unique index udx_name (`name`, `deleted_timestamp`),
    unique index udx_code (`code`)
) comment '系统用户表';

create table `role`
(
    `id`                bigint      not null auto_increment primary key,
    `code`              varchar(32) not null,
    `super_admin`       bit         not null default 0,
    `name`              varchar(64) not null,
    `sort`              int                  default 1,
    `status`            int                  default 1,
    `create_time`       datetime    not null,
    `update_time`       datetime    not null,
    `deleted_timestamp` bigint      not null default 0 comment '删除时间戳，0表示未删除',
    unique index udx_name_deleted (`name`, `deleted_timestamp`)
) comment '角色（岗位）';

create table `user_role`
(
    `id`      bigint not null auto_increment primary key,
    `user_id` bigint not null,
    `role_id` bigint not null,
    unique index udx_user_role (`user_id`),
    index idx_role (`role_id`)
) comment '用户-角色（岗位）关联关系表';

-- 初始化用户：admin，id：1，密码：123456
insert into `user`
(`code`, `name`, `nick_name`, `english_name`, `gender`, `birthday`, `password`,
 `creator`, `creator_code`, `create_time`, `updater`, `updater_code`, `update_time`)
values ('000000', 'admin', '超级管理员', 'admin',
        1, '2023-08-01',
        '5f39487f11580c14e7a3eff5e80156cab91c66153be64c674a48444a087b0f96', 'admin',
        '000000', now(), 'admin', '000000', now());
-- 初始化用户：admin，id：1，密码：123456

-- 初始化角色：超级管理员，id：1，code：SUPER_ADMIN
insert into `role`
(`code`, `super_admin`, `name`, `sort`, `status`, `create_time`, `update_time`)
VALUES ('SUPER_ADMIN', 1, '超级管理员', 0, 1, now(), now());
-- 初始化角色：超级管理员，id：1，code：SUPER_ADMIN

insert into `user_role`(`user_id`, `role_id`)
VALUES (1, 1);
