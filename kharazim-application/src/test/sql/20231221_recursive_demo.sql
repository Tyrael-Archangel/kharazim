create table recursive_demo_address
(
    `id`          bigint      not null auto_increment primary key,
    `name`        varchar(32) not null unique key,
    `parent_id`   bigint
);


-- 四川省
--    成都市
--       武侯区
--       锦江区
--       成华区
--    广元市
--       朝天区
--       剑阁县
--       青川县
-- 北京
--    北京市
--        朝阳区
--        海淀区


insert into recursive_demo_address(`name`, `parent_id`) values ('四川省', null);
insert into recursive_demo_address(`name`, `parent_id`) select '成都市', id from recursive_demo_address where `name` = '四川省';
insert into recursive_demo_address(`name`, `parent_id`) select '武侯区', id from recursive_demo_address where `name` = '成都市';
insert into recursive_demo_address(`name`, `parent_id`) select '锦江区', id from recursive_demo_address where `name` = '成都市';
insert into recursive_demo_address(`name`, `parent_id`) select '成华区', id from recursive_demo_address where `name` = '成都市';
insert into recursive_demo_address(`name`, `parent_id`) select '广元市', id from recursive_demo_address where `name` = '四川省';
insert into recursive_demo_address(`name`, `parent_id`) select '朝天区', id from recursive_demo_address where `name` = '广元市';
insert into recursive_demo_address(`name`, `parent_id`) select '剑阁县', id from recursive_demo_address where `name` = '广元市';
insert into recursive_demo_address(`name`, `parent_id`) select '青川县', id from recursive_demo_address where `name` = '广元市';
insert into recursive_demo_address(`name`, `parent_id`) values ('北京', null);
insert into recursive_demo_address(`name`, `parent_id`) select '北京市', id from recursive_demo_address where `name` = '北京';
insert into recursive_demo_address(`name`, `parent_id`) select '朝阳区', id from recursive_demo_address where `name` = '北京市';
insert into recursive_demo_address(`name`, `parent_id`) select '海淀区', id from recursive_demo_address where `name` = '北京市';


with recursive demo_address as (
    select * from recursive_demo_address where `name` = '四川省'
    union all
    select m.* from recursive_demo_address m inner join demo_address rm on m.parent_id = rm.id
)
select *
from demo_address;

-- 凭空创建数据
with recursive t_date (i, date) as (
    select 1, current_date
    union all
    select i + 1, date_sub(date, interval 1 day) from t_date where i < 7
)
select *
from t_date;