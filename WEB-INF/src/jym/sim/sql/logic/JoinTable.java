// CatfoOD 2011-7-8 下午02:05:34 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.logic;

import jym.sim.orm.ISelectJoin;


public class JoinTable implements ISelectJoin {

	private DefinitionLogic where;
	private String joinColumn;
	private String tableName;
	
	private String mainTable;
	private String mainColumn;
	
	private String cache_join;
	

	public JoinTable(String tableName, String joinColumn, DefinitionLogic where) {
		if (tableName  ==null) throw new NullPointerException();
		if (joinColumn ==null) throw new NullPointerException();
		if (where      ==null) throw new NullPointerException();
		
		this.tableName  = tableName;
		this.joinColumn = joinColumn;
		this.where      = where;
	}

	public String getJoin() {
		if (cache_join==null) {
			if (mainTable ==null) throw new NullPointerException();
			if (mainColumn==null) throw new NullPointerException();
			
			StringBuilder sql = new StringBuilder();
			sql.append( " left join "	);
			sql.append(  tableName		);
			sql.append( " on "			);
			sql.append(  mainTable ).append('.').append(mainColumn);
			sql.append( " = ");
			sql.append(  tableName ).append('.').append(joinColumn).append(' ');
			
			cache_join = sql.toString();
		}
		return cache_join;
	}
	
	public String getWhere(Object model) {
		if (where==null) {
			return null;
		}
		return where.w(joinColumn, null, model);
	}
	
	public void setMainTable(String tableName) {
		mainTable = tableName;
	}
	
	public void setMainColumn(String columnName) {
		mainColumn = columnName;
	}
	
}
