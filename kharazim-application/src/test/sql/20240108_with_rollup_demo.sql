create table `demo_movies`
(
    `id`    bigint         not null auto_increment primary key,
    `name`  varchar(32)    not null,
    `price` decimal(10, 2) not null
);

insert into `demo_movies`(`name`, `price`)
values ('Spider Man', 38.88),
       ('Bat Man', 42.99),
       ('Super Man', 51.98);

select `name`, sum(`price`)
from `demo_movies`
group by `name`
with rollup;
