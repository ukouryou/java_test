set SQL_LOG_BIN = 0;
SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;

drop table if exists libin_skill;
drop table if exists libin_s_t_user;

create table libin_skill(user_id int,skill_id int,`name` varchar(255),level int);
insert into libin_skill select sku.user_id,sku.skill_id,sk.name, sku.level from tianji.skills_users as sku inner join tianji.skills as sk on sku.skill_id = sk.id;

create index libin_skill_user_id_idx on libin_skill(user_id);

create table libin_s_t_user(user_id integer,type char(1));

insert into libin_s_t_user
select V.member_id,V.t_type
from (
  select member_id,
      (case when m.agency is null then 's'
        when m.agency is not null and m.agency not REGEXP '^k' then 't'
        else
          'b'
       end ) as t_type
  from tianji_kpi.member m
) V where t_type != 'b';

create index libin_s_t_user_user_id_idx on libin_s_t_user(user_id);