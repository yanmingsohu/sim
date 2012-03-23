// CatfoOD 2012-3-22 上午10:43:27 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm.page;

import jym.sim.orm.IPlot;


public class SQLServerPagination implements IPage {
	
	private final static String ROW_NUM_COL = IPlot.PASS_COLM_NAME + "row_number";
	private String orderColName;

	/**
	 * 自动使用该表的第一个字段作为开窗排序列
	 */
	public SQLServerPagination() {	
		orderColName = null;
	}
	
	/**
	 * 使用固定的字段作为开窗排序列, 不需要完整的表名引用
	 * 该列在orm不提供key的情况下被使用
	 */
	public SQLServerPagination(String row_number_order_column) {
		orderColName = row_number_order_column;
	}
	
	public String select(PaginationParam parm) {
		
		PageBean page = parm.getPage();
		int s = (page.getCurrent() - 1) * page.getOnesize();
		int e = s + page.getOnesize();

		StringBuilder z = new StringBuilder();
		
		z.append("	with t_rowtable "									);
		z.append("	as ("												);
		z.append("		select row_number() over("						);
		
		fillOverOrder(z, parm);
		
		z.append("		) as "		).append( ROW_NUM_COL ).append( ',' );
		z.append(		parm.getTableName() ).append( ".* from "		);
		z.append(		parm.getTableName()								);
		z.append(		BLANK											);
		z.append(		parm.getJoin()									);
		z.append(		BLANK											);
		z.append(		parm.getWhereSub()								);
		z.append("	)"													);
		z.append("	select * from t_rowtable"							);
		z.append("	where " ).append(ROW_NUM_COL).append(" > ").append(s);
		z.append("	and "   ).append(ROW_NUM_COL).append(" <=").append(e);
		
		return z.toString();
	}
	
	private void fillOverOrder(final StringBuilder z, PaginationParam parm) {
		String order = parm.getOrder();
		
		if (order.length() > 5) {
			z.append(order);
		}
		else if (parm.getKey() != null) {
			z.append("order by ");
			z.append(parm.getTableName()).append('.');
			z.append(parm.getKey());
		}
		else if (orderColName != null) {
			z.append("order by ");
			z.append(parm.getTableName()).append('.');
			z.append(orderColName);
		} 
		else {
			z.append("		order by ("						);
			z.append("			select top 1 col.name"		);
			z.append("			from syscolumns col"		);
			z.append("			join sysobjects tbl"		);
			z.append("			on col.id = tbl.id"			);
			z.append("			where tbl.name = '"			)
			 .append( 	parm.getTableName() ).append( "'"	);
			z.append("		)"								);
		}
	}

	public String selectTotalPage(PaginationParam parm) {
		return "select count(*) " 
				+ TOTAL_COLUMN_NAME 
				+ " from " 
				+ parm.getTableName() 
				+ BLANK
				+ parm.getJoin() 
				+ BLANK
				+ parm.getWhereSub();
	}

}
