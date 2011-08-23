-- 用来做转换测试

UPDATE 	bp_customer_info, 'a>b' 
SET 	dist_sn= '653' , doc_id= 1 , cust_state='0' , cust_name='ttt' , 
		cust_address='联调_200_0605' , bank_id=null , cust_email=null , cust_fax=null , 
		cust_moblie=null , cust_note='批量开户用户' , humen_sum='1' , id_card='84555' , 
		pay_method='1' , tax_id='1' , update_date='2011-08-22' , l=null, a>=1, b<=2, c<>3,
		cc='a>b', eee like 'bb'
WHERE 	cust_sn= '64306';


select *
from (
  select rownum sim__row__num, sim__in__table.*
  from (
    select bp_customer_info.*
    from bp_customer_info left
    join V_AREA
     on bp_customer_info.dist_sn = V_AREA.DIST_SN left
    join V_OPER_TO_CUST
     on bp_customer_info.cust_sn = V_OPER_TO_CUST.CUST_SN
    where (
      V_OPER_TO_CUST.OPERATOR_SN = 61
    ) and (
      bp_customer_info.cust_state = '0'
    )
  ) sim__in__table
  where rownum <= 30
),(
  select count(
    1
  ) sim__total__row
  from bp_customer_info left
  join V_AREA
   on bp_customer_info.dist_sn = V_AREA.DIST_SN left
  join V_OPER_TO_CUST
   on bp_customer_info.cust_sn = V_OPER_TO_CUST.CUST_SN
  where (
    V_OPER_TO_CUST.OPERATOR_SN = 61
  ) and (
    bp_customer_info.cust_state = '0'
  )
);


INSERT INTO bm_meter_factory(
  fac_name,fac_address,fac_id
)VALUES(
  'a',   'b'   ,1,2,
)