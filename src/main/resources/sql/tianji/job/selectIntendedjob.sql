select 
v1.user_id,
v1.f_value,
v1.p_value,
v1.l_value,
v1.s_value,
v2.f,
v2.p,
v2.l,
v2.s,
v2.job_id
from
(select 
V.user_id,
(select 
intention_value
from
tianji.position_intention
where
intention_name = 'f'
and user_id = V.user_id
limit 1) as f_value,
(select 
intention_value
from
tianji.position_intention
where
intention_name = 'p'
and user_id = V.user_id
limit 1) as p_value,
(select 
intention_value
from
tianji.position_intention
where
intention_name = 'l'
and user_id = V.user_id
limit 1) as l_value,
(select 
intention_value
from
tianji.position_intention
where
intention_name = 's'
and user_id = V.user_id
limit 1) as s_value
from
(select distinct
user_id
from
tianji.position_intention where is_visible is false ) V) v1,
(select 
id as job_id,
field as f,
salary_from as s,
position_enum as p,
concat_ws('-', country, province, city) as l
from
tianji.positions
where
id in (55970 , 65569, 65570, 65546, 65388, 65448, 65427, 65450, 55705, 63620, 62474, 65693, 65687, 65686, 65766, 65765, 65763, 65852, 65787, 65792, 49484, 65877, 65892, 65931)) v2
where
1 = 1
and find_in_set(v2.s, v1.s_value) > 0
and find_in_set(v2.p, v1.p_value) > 0
and find_in_set(v2.f, v1.f_value) > 0
and find_in_set(v2.l, v1.l_value) > 0;
