drop table if exists `demo_row_number_sale_order`;
create table `demo_row_number_sale_order`
(
    `id`   bigint      not null primary key auto_increment,
    `code` varchar(32) not null
);

drop table if exists `demo_row_number_sale_order_item`;
create table `demo_row_number_sale_order_item`
(
    `id`           bigint      not null primary key auto_increment,
    `order_id`     bigint      not null,
    `product_name` varchar(32) not null,
    `quantity`     int         not null,
    index idx_order_id (`order_id`)
);

drop table if exists `demo_row_number_sale_order_receive`;
create table `demo_row_number_sale_order_receive`
(
    `id`            bigint not null primary key auto_increment,
    `order_item_id` bigint not null,
    `quantity`      int    not null,
    `receive_date`  date   not null,
    index idx_order_item_id (`order_item_id`)
);

insert into `demo_row_number_sale_order`(`id`, `code`)
values (1, 'SO-001'),
       (2, 'SO-002'),
       (3, 'SO-003');

insert into `demo_row_number_sale_order_item`
    (`id`, `order_id`, `product_name`, `quantity`)
VALUES (1, 1, 'keyboard', 2),
       (2, 1, 'mouse', 3),
       (3, 1, 'earphone', 1),
       (4, 2, 'keyboard', 4),
       (5, 2, 'earphone', 2),
       (6, 3, 'earphone', 2),
       (7, 3, 'phone', 2),
       (8, 3, 'monitor', 2),
       (9, 3, 'USB cable', 10);

insert into `demo_row_number_sale_order_receive`
    (`order_item_id`, `quantity`, `receive_date`)
VALUES (1, 1, '2024-01-10'),
       (1, 1, '2024-01-12'),
       (2, 1, '2024-01-11'),
       (2, 2, '2024-01-12'),
       (3, 1, '2024-01-12'),
       (4, 4, '2024-01-14'),
       (5, 2, '2024-01-11'),
       (9, 6, '2024-01-15'),
       (9, 3, '2024-01-15');


select o.id                                                        as o_id,
       o.code                                                      as o_code,
       oi.id                                                       as oi_id,
       oi.product_name,
       oi.quantity                                                 as oi_quantity,
       sum(ifnull(sor.quantity, 0))                                as oi_receive_quantity,
       sor.receive_date,
       row_number() over (partition by o.id order by oi.id)        as order_row,
       row_number() over (partition by o.id, oi.id order by oi.id) as order_item_row
from `demo_row_number_sale_order` o
         inner join `demo_row_number_sale_order_item` oi on o.id = oi.order_id
         left join `demo_row_number_sale_order_receive` sor on oi.id = sor.order_item_id
group by o.id, oi.id, sor.receive_date
order by o.id desc, oi.id, sor.receive_date;

