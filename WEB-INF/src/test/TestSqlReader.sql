SELECT MDATA_VAL 数据, GDAY 日期, GDATE 时间 FROM   --注释可用
(
	SELECT COUNT(MDATA_VAL) over
	(
		partition BY GDATE order by GDATE
	) mdata_count, MDATA_VAL, GDAY, GDATE   
	FROM     
	( 
		SELECT aa.MDATA_VAL, aa.GDAY,       
			DECODE(aa.GDATE, NULL, bb.GDATE, aa.GDATE) GDATE     
		FROM       
		(
			SELECT SUBSTR(TO_CHAR(VI.GETDATE, 'hh24:mi:ss'), 1, 6) 
				|| '00' GDATE, VI.MDATA_VAL, TO_CHAR(VI.GETDATE, 'yyyy-mm-dd') GDAY        
			FROM  V_H_DATA_DETAIL VI        
			JOIN  CS_AREA_USER TI        
			ON    VI.AREA_USER_SN = TI.AREA_USER_SN
			WHERE TI.AREA_USER_SN = ${data.as() }	   
			AND   DATA_KIND_ID    = '${data.kindId }'
			AND 
			(  
		--	#for (	d	,		data.dates	) 
		--	^命令 ^参数表开始  ^参数列表		^参数表结尾	^命令体
		
		--  #之后紧接着一个命令字符串, 逗号, 命令参数1, 逗号, 命令参数n
		--  命令以'#'开始, 以参数列表')'结尾
		
				${data.dates}
		--	#end -- 所有命令都使用end结束
			)
		) aa partition BY(aa.gday)     
		RIGHT JOIN       
		(
			SELECT GDATE       
			FROM         
			(
				SELECT TO_CHAR(TRUNC(sysdate, 'dd')
					+ ((level - 1) / (24 * 60)), 'hh24:mi:ss') GDATE          
				FROM dual CONNECT BY level <= 60 * 24
			)       
		) bb     
		ON aa.GDATE = bb.GDATE     
	)   
) 
WHERE mdata_count > 0 
ORDER BY GDAY, GDATE
