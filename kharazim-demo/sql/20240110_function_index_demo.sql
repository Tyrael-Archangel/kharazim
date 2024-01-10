create table `demo_system_request_log`
(
    `id`         bigint not null primary key auto_increment,
    `uri`        varchar(2048),
    `start_time` datetime(3),
    `end_time`   datetime(3),
    `cost_mills` int as ((`end_time` - `start_time`) * 1000),
    index fi_cost_mills ((`end_time` - `start_time`))
);

explain select *
from `demo_system_request_log`
where ((`end_time` - `start_time`)) = 0.123;
