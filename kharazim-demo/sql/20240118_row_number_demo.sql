drop table if exists demo_row_number_sale_order;
create table demo_row_number_sale_order
(
    `id`   bigint      not null primary key auto_increment,
    `code` varchar(32) not null
);

drop table if exists demo_row_number_sale_order_item;
create table demo_row_number_sale_order_item
(
    `id`           bigint      not null primary key auto_increment,
    `order_id`     bigint(32)  not null,
    `product_name` varchar(32) not null,
    `quantity`     int         not null,
    index idx_order_id (`order_id`)
);

insert into demo_row_number_sale_order(`id`, `code`)
values (1, 'SO-001'),
       (2, 'SO-002'),
       (3, 'SO-003');

insert into demo_row_number_sale_order_item
    (`order_id`, `product_name`, `quantity`)
VALUES (1, 'keyboard', 2),
       (1, 'mouse', 3),
       (1, 'earphone', 1),
       (2, 'keyboard', 4),
       (2, 'earphone', 2),
       (3, 'earphone', 2),
       (3, 'phone', 2),
       (3, 'monitor', 2),
       (3, 'USB cable', 10);

select o.id,
       o.code,
       oi.id,
       oi.product_name,
       oi.quantity,
       row_number() over (partition by o.id order by oi.id) as order_row
from `demo_row_number_sale_order` o
         inner join `demo_row_number_sale_order_item` oi on o.id = oi.order_id
order by o.id desc, oi.id;


