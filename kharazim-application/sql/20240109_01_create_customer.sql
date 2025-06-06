create table `customer`
(
    `id`                   bigint      not null auto_increment primary key,
    `code`                 varchar(32) not null comment '会员编码',
    `name`                 varchar(64) not null comment '会员名称',
    `gender`               int comment '性别',
    `birth_year`           int comment '生日年',
    `birth_month`          int comment '生日月',
    `birth_day_of_month`   int comment '生日日',
    `phone`                varchar(32) comment '手机号码',
    `phone_verified`       bit comment '手机号是否已验证',
    `certificate_type`     int comment '证件类型',
    `certificate_code`     varchar(64) comment '证件号',
    `wechat_code`          varchar(64) comment '微信号',
    `wechat_union_id`      varchar(64) comment '微信union_id',
    `wechat_name`          varchar(64) comment '微信名',
    `source_channel_dict`  varchar(32) comment '来源渠道字典值',
    `source_customer_code` varchar(32) comment '推荐（引导）来源会员编码',
    `remark`               varchar(255) comment '备注',
    `creator`              varchar(64),
    `creator_code`         varchar(32),
    `create_time`          datetime,
    `updater`              varchar(64),
    `updater_code`         varchar(32),
    `update_time`          datetime,
    `deleted`              bigint      not null default 0 comment '删除时间，0表示未删除',
    unique index udx_code (`code`),
    index idx_name (`name`),
    index idx_certificate_code (`certificate_code`),
    index idx_phone (`phone`)
) comment '会员';

insert into `dict_item`(`key`, `value`, `dict_code`, `sort`)
values ('OFFLINE', '线下', 'customer_source_channel', 1),
       ('OTHER', '其他', 'customer_source_channel', 100);

create table `customer_address`
(
    `id`              bigint      not null auto_increment primary key,
    `customer_code`   varchar(32) not null comment '会员编码',
    `contact`         varchar(64) comment '联系人',
    `contact_phone`   varchar(32) comment '联系人电话',
    `province_code`   varchar(32) comment '省份编码',
    `province_name`   varchar(64) comment '省份名称',
    `city_code`       varchar(32) comment '城市编码',
    `city_name`       varchar(64) comment '城市名称',
    `county_code`     varchar(32) comment '县（区）编码',
    `county_name`     varchar(64) comment '县（区）名称',
    `detail_address`  varchar(255) comment '详细地址',
    `default_address` bit         not null default 0 comment '是否为会员的默认地址',
    `creator`         varchar(64),
    `creator_code`    varchar(32),
    `create_time`     datetime,
    `updater`         varchar(64),
    `updater_code`    varchar(32),
    `update_time`     datetime,
    `deleted`         bigint      not null default 0 comment '删除时间，0表示未删除',
    index idx_customer_code (`customer_code`)
) comment '会员地址';

create table `customer_insurance`
(
    `id`                bigint      not null auto_increment primary key,
    `customer_code`     varchar(32) not null comment '会员编码',
    `company_dict`      varchar(64) comment '保险公司字典值',
    `policy_number`     varchar(64) comment '保单号',
    `duration`          date comment '保险有效期限',
    `benefits`          varchar(1024) comment '保险福利',
    `default_insurance` bit         not null default 0 comment '是否为会员的默认保险',
    `creator`           varchar(64),
    `creator_code`      varchar(32),
    `create_time`       datetime,
    `updater`           varchar(64),
    `updater_code`      varchar(32),
    `update_time`       datetime,
    `deleted`           bigint      not null default 0 comment '删除时间，0表示未删除',
    unique index udx_customer_policy (`customer_code`, `policy_number`, `deleted`)
) comment '会员保险';

create table `customer_service_user`
(
    `id`                bigint      not null auto_increment primary key,
    `customer_code`     varchar(32) not null comment '会员编码',
    `service_user_code` varchar(32) not null comment '专属客服编码',
    `creator`           varchar(64),
    `creator_code`      varchar(32),
    `create_time`       datetime,
    `updater`           varchar(64),
    `updater_code`      varchar(32),
    `update_time`       datetime,
    `deleted`           bigint      not null default 0 comment '删除时间，0表示未删除',
    unique index udx_customer_code (`customer_code`, `deleted`)
) comment '会员专属客服';

create table `customer_sales_consultant`
(
    `id`                    bigint      not null auto_increment primary key,
    `customer_code`         varchar(32) not null comment '会员编码',
    `sales_consultant_code` varchar(32) not null comment '专属销售顾问编码',
    `creator`               varchar(64),
    `creator_code`          varchar(32),
    `create_time`           datetime,
    `updater`               varchar(64),
    `updater_code`          varchar(32),
    `update_time`           datetime,
    `deleted`               bigint      not null default 0 comment '删除时间，0表示未删除',
    unique index udx_customer_code (`customer_code`, `deleted`)
) comment '会员专属销售顾问';

create table `customer_tag`
(
    `id`            bigint      not null auto_increment primary key,
    `customer_code` varchar(32) not null comment '会员编码',
    `tag_dict`      varchar(64) not null comment '会员标签字典值',
    `creator`       varchar(64),
    `creator_code`  varchar(32),
    `create_time`   datetime,
    `updater`       varchar(64),
    `updater_code`  varchar(32),
    `update_time`   datetime,
    `deleted`       bigint      not null default 0 comment '删除时间，0表示未删除',
    unique index udx_customer_tag (`customer_code`, `tag_dict`, `deleted`)
) comment '会员标签';

create table `customer_communication_log`
(
    `id`                 bigint      not null auto_increment primary key,
    `type_dict`          varchar(64) comment '沟通类型字典值',
    `customer_code`      varchar(32) not null comment '会员编码',
    `service_user_code`  varchar(32) not null comment '客服人员编码',
    `content`            varchar(1024) comment '沟通内容',
    `evaluate_dict`      varchar(64) comment '沟通评价字典值',
    `communication_time` datetime    not null comment '沟通时间',
    `creator`            varchar(64) comment '创建人',
    `creator_code`       varchar(32) comment '创建人编码',
    `create_time`        datetime comment '创建时间',
    index idx_customer_code (`customer_code`),
    index idx_create_time (`create_time`)
) comment '会员沟通记录';

create table `family`
(
    `id`           bigint      not null auto_increment primary key,
    `code`         varchar(32) not null comment '家庭编码',
    `name`         varchar(64) comment '家庭名',
    `leader_code`  varchar(32) not null comment '户主会员编码',
    `remark`       varchar(1024),
    `creator`      varchar(64),
    `creator_code` varchar(32),
    `create_time`  datetime,
    `updater`      varchar(64),
    `updater_code` varchar(32),
    `update_time`  datetime,
    `deleted`      bigint      not null default 0 comment '删除时间，0表示未删除',
    unique index udx_code (`code`)
) comment '家庭';

create table `family_member`
(
    `id`                 bigint      not null auto_increment primary key,
    `family_code`        varchar(32) not null comment '家庭编码',
    `customer_code`      varchar(32) not null comment '会员编码',
    `relation_to_leader` varchar(32) not null comment '与家庭户主关系',
    `creator`            varchar(64),
    `creator_code`       varchar(32),
    `create_time`        datetime,
    `updater`            varchar(64),
    `updater_code`       varchar(32),
    `update_time`        datetime,
    `deleted`            bigint      not null default 0 comment '删除时间，0表示未删除',
    unique index udx_customer_family (`customer_code`, `family_code`, `deleted`),
    index idx_family (`family_code`)
) comment '家庭成员';
