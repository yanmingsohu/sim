// CatfoOD 2011-7-25 下午03:04:49 yanming-sohu@sohu.com/@qq.com

package jym.sim.test.sql;

import jym.sim.util.SqlFormat;
import jym.sim.util.Tools;


public class TestSqlFormat {

	static String sql = 
		"select * from (\n  select rownum sim__row__num, sim__in__table.* from " +
		"(select bp_customer_info.* from bp_customer_info  " +
		"left join V_AREA on bp_customer_info.dist_sn = V_AREA.DIST_SN  " +
		"left join V_OPER_TO_CUST on bp_customer_info.cust_sn = V_OPER_TO_CUST.CUST_SN  " +
		"where (V_AREA.BRONGTH_SN = 1701) and (V_OPER_TO_CUST.OPERATOR_SN = 61)   ) sim__in__table " +
		"where rownum <= 30 ),( select count(1) sim__total__row from bp_customer_info  " +
		"left join V_AREA on bp_customer_info.dist_sn = V_AREA.DIST_SN  " +
		"left join V_OPER_TO_CUST on bp_customer_info.cust_sn = V_OPER_TO_CUST.CUST_SN  " +
		"where (V_AREA.BRONGTH_SN = 1701) and (V_OPER_TO_CUST.OPERATOR_SN = 61)  ) " +
		"where sim__row__num >= 1";
	
	
	public static void main(String[] args) {
		System.out.println(SqlFormat.format(sql));
		Tools.plsql(sql);
	}

}
