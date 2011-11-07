// CatfoOD 2011-7-8 下午02:05:34 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql.logic;

import jym.sim.orm.ISelectJoin;
import jym.sim.sql.IWhere;


public class JoinTable implements ISelectJoin {
	
	public final static class JoinType {
		public static final JoinType JOIN 		= new JoinType(" join ");
		public static final JoinType LEFT_JOIN 	= new JoinType(" left join ");
		public static final JoinType RIGHT_JOIN	= new JoinType(" right join ");
		
		private String type;
		
		private JoinType(String tname) {type=tname;}
	}

	
	private IWhere where;
	private String joinColumn;
	private String tableName;
	
	private String mainTable;
	private String mainColumn;
	
	private String cache_join;
	private String joinType;
	

	/** 默认为LEFT_JOIN */
	public JoinTable(String tableName, String joinColumn, IWhere where) {
		this(JoinType.LEFT_JOIN, tableName, joinColumn, where);
	}
	
	public JoinTable(JoinType joinType, String tableName, String joinColumn, IWhere where) {
		if (tableName  ==null) throw new NullPointerException();
		if (joinColumn ==null) throw new NullPointerException();
	//	if (where      ==null) throw new NullPointerException();
		
		this.tableName  = tableName;
		this.joinColumn = joinColumn;
		this.where      = where;
		this.joinType	= joinType.type;
	}

	public String getJoin() {
		if (cache_join==null) {
			if (mainTable ==null) throw new NullPointerException();
			if (mainColumn==null) throw new NullPointerException();
			
			StringBuilder sql = new StringBuilder();
			sql.append(  joinType		);
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
