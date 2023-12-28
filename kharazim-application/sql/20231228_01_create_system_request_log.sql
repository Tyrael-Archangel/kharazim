create table `system_request_log`
(
    `id`               bigint not null primary key auto_increment,
    `uri`              varchar(2048),
    `http_method`      varchar(16),
    `remote_addr`      varchar(128),
    `real_ip`          varchar(128),
    `forwarded_for`    varchar(128),
    `request_headers`  json,
    `response_headers` json,
    `request_params`   json,
    `response_status`  int,
    `request_body`     longtext,
    `response_body`    longtext,
    `user_name`        varchar(64),
    `start_time`       datetime(3),
    `end_time`         datetime(3),
    `cost_mills`       int as (((`end_time` - `start_time`) * 1000)),
    index idx_start (`start_time`),
    index idx_end (`end_time`)
) comment '系统请求日志';