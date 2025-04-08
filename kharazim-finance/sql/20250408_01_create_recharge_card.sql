create table recharge_card_type
(
    `id`                  bigint         not null auto_increment primary key,
    `code`                varchar(32)    not null comment '储值卡项编码',
    `name`                varchar(64)    not null comment '储值卡项名称',
    `discount_percent`    decimal(4, 2)  not null comment '折扣百分比',
    `never_expire`        bit            not null comment '是否永不过期',
    `valid_period_days`   int comment '有效期天数',
    `default_amount`      decimal(10, 2) not null comment '默认卡金额',
    `can_create_new_card` bit            not null comment '是否可以创建新卡',
    `creator`             varchar(64)    not null,
    `creator_code`        varchar(32)    not null,
    `create_time`         datetime       not null,
    `updater`             varchar(64)    not null,
    `updater_code`        varchar(32)    not null,
    `update_time`         datetime       not null,
    `deleted`             bigint         not null default 0 comment '删除时间，0表示未删除',
    unique index udx_code (`code`)
) comment '储值卡项';

create table `customer_recharge_card`
(
    `id`                       bigint                  not null auto_increment primary key,
    `code`                     varchar(32)             not null comment '储值单号',
    `status`                   int                     not null comment '状态',
    `customer_code`            varchar(32)             not null comment '会员编码',
    `card_type_code`           varchar(32)             not null comment '储值卡项编码',
    `total_amount`             decimal(10, 2) unsigned not null comment '储值总金额',
    `consumed_amount`          decimal(10, 2) unsigned not null comment '已消费金额',
    `consumed_original_amount` decimal(10, 2) unsigned not null comment '已消费商品原价金额',
    `trader_user_code`         varchar(32)             not null comment '成交员工编码',
    `discount_percent`         decimal(4, 2)           not null comment '折扣百分比',
    `never_expire`             bit                     not null comment '是否永不过期',
    `expire_date`              date comment '过期时间',
    `recharge_date`            date                    not null comment '储值日期',
    `chargeback_amount`        decimal(10, 2) unsigned comment '退卡金额',
    `chargeback_user_code`     varchar(64) comment '退卡员工编码',
    `creator`                  varchar(64),
    `creator_code`             varchar(32),
    `create_time`              datetime,
    `updater`                  varchar(64),
    `updater_code`             varchar(32),
    `update_time`              datetime,
    `deleted`                  bigint                  not null default 0 comment '删除时间，0表示未删除',
    unique index udx_code (`code`),
    index idx_customer (`customer_code`)
) comment '会员储值单';

create table `customer_recharge_card_log`
(
    `id`                   bigint         not null auto_increment primary key,
    `recharge_card_code`   varchar(32)    not null comment '储值单编号',
    `customer_code`        varchar(32)    not null comment '会员编码',
    `log_type`             int            not null comment '日志类型',
    `source_business_code` varchar(32) comment '关联的业务单号，例如结算单号',
    `create_time`          datetime       not null comment '创建时间',
    `amount`               decimal(10, 2) not null comment '交易金额',
    `operator`             varchar(64)    not null,
    `operator_code`        varchar(32)    not null,
    `remark`               varchar(255),
    index idx_recharge_card_code (`recharge_card_code`),
    index idx_create_time (`create_time`)
) comment '储值单日志记录';

create table `customer_wallet_transaction`
(
    `id`                   bigint         not null auto_increment primary key,
    `code`                 varchar(32)    not null comment '交易流水号',
    `customer_code`        varchar(32)    not null comment '会员编码',
    `type`                 int            not null comment '交易类型，充值、消费、退款',
    `transaction_time`     datetime       not null comment '交易时间',
    `source`               int            not null comment '关联的业务类型（储值单、结算单）',
    `source_business_code` varchar(32)    not null comment '关联的业务单号',
    `amount`               decimal(10, 2) not null comment '交易金额',
    `operator`             varchar(64),
    `operator_code`        varchar(32),
    `remark`               varchar(255),
    index idx_code (`code`),
    index idx_customer (`customer_code`),
    index idx_transaction_time (`transaction_time`)
) comment '会员交易流水';

