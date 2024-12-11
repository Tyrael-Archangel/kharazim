create table `menu`
(
    `id`          bigint      not null auto_increment primary key,
    `parent_id`   bigint      not null default -1,
    `menu_type`   int         not null comment '菜单类型，1-菜单，2-目录，3-外链，4-按钮',
    `name`        varchar(64) not null,
    `path`        varchar(128),
    `component`   varchar(128),
    `perm`        varchar(64) comment '权限标识',
    `icon`        varchar(64),
    `sort`        int                  default 0 comment '排序号',
    `visible`     bit                  default 1,
    `redirect`    varchar(128),
    `create_time` datetime    not null,
    `update_time` datetime    not null,
    unique index udx_name (`name`, `parent_id`)
) comment '系统菜单表';

create table `role_menu`
(
    `id`      bigint not null auto_increment primary key,
    `role_id` bigint not null,
    `menu_id` bigint not null,
    unique index udx_user_role (`role_id`, `menu_id`)
) comment '角色（岗位）-菜单关联关系表';
