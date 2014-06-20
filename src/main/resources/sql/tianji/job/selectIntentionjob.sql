select v1.user_id from 
	(select V.user_id, 
	(select intention_value	from tianji.position_intention where intention_name = 'f' and user_id = V.user_id limit 1) as f_value,
	(select intention_value from tianji.position_intention where intention_name = 'p' and user_id = V.user_id limit 1) as p_value,
	(select intention_value from tianji.position_intention where intention_name = 'l' and user_id = V.user_id limit 1) as l_value,
	(select intention_value from tianji.position_intention where intention_name = 's' and user_id = V.user_id limit 1) as s_value
	from (select distinct user_id from tianji.position_intention where is_visible is true ) V) v1 where v1.s_value in (3000,8000)  and v1.f_value in (101,104,102,103,106) and p_value in (8,4,10,9,39) and l_value in ('CN-11-北京','CN-12-天津') limit 5;
