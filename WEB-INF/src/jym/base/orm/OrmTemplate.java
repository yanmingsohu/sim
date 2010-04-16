package jym.base.orm;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import jym.base.sql.ISql;
import jym.base.sql.JdbcTemplate;
import jym.base.sql.Logic;
import jym.base.util.BeanUtil;
import jym.base.util.Tools;

/**
 * 数据库实体映射模板
 */
public class OrmTemplate<T> implements ISelecter<T> {

	private JdbcTemplate jdbc;
	private Class<T> clazz;
	private IOrm<T> orm;
	private Plot<T> plot;
	
	
	/**
	 * jdbc模板构造函数, 全部使用表格名映射实体属性
	 * 
	 * @param ds - 数据源
	 * @param modelclass - 数据模型的class类
	 * @param simSql - 特殊格式的查询语句,见{@link jym.base.orm.IOrm}
	 */
	public OrmTemplate(DataSource ds, final Class<T> modelclass, final String simSql) {
		this(ds, new IOrm<T>() {

			public Class<T> getModelClass() {
				return modelclass;
			}

			public String getSimSql() {
				return simSql;
			}

			public void mapping(IPlot plot) {
			}
		});
	}
	
	/**
	 * jdbc模板构造函数,默认每次连接不会自动关闭连接
	 * 
	 * @param orm - 数据库列数据与bean实体属性映射策略
	 * @throws SQLException - 数据库错误抛出异常
	 */
	public OrmTemplate(DataSource ds, IOrm<T> orm) {
		jdbc = new JdbcTemplate(ds);
		this.orm = orm;
		
		init();
	}
	
	private void init() {
		clazz = orm.getModelClass();
		plot = new Plot<T>(orm);
		
		checkSql();
	}
	
	private void checkSql() {
		String simsql = orm.getSimSql();
		if ( simsql.indexOf("$where") < 0 ) {
			warnning("sql语句中不含有$where变量");
		}
		if ( simsql.indexOf(" where ")>=0 ) {
			throw new IllegalArgumentException("sql语句中不能含有where子句");
		}
	}
	
	public List<T> select(T model, String join) {
		
		Method[] ms = model.getClass().getMethods();
		final StringBuilder where = new StringBuilder(" where ");
		
		boolean first = true;
		
		for (int i=0; i<ms.length; ++i) {
			String colname = plot.getColname(ms[i]);
		
			if (colname!=null) {
				try {
					Object value = ms[i].invoke(model, new Object[0]);
					
					if ( BeanUtil.isValid(value) ) {
						if (first) {
							first = false;
						} else {
							where.append(join);
						}
						Logic logic = plot.getColumnLogic(colname);
						where.append( " (" ).append(colname).append(' ')
								.append(logic.in(value)).append( ") " );
					}
					
				} catch (Exception e) {
					warnning("invoke错误: "+ e);
				}
			}
		}
		
		return select(where.toString());
	}
	
	public List<T> select(final String where) {
		final List<T> brs = new ArrayList<T>();
		
		jdbc.query(new ISql() {
			public void exception(Throwable tr, String msg) {
				warnning("select错误: " + msg);
			}

			public void exe(Statement stm) throws Throwable {
				String sql = swapwhere(orm.getSimSql(), where);
				Tools.plsql(sql);
				select( stm.executeQuery(sql), brs );
			}
		});
		
		return brs;
	}
	
	private void select(ResultSet rs, List<T> brs) throws Exception {
		if (rs==null) return;
		
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int col = rsmd.getColumnCount();
			
			while ( rs.next() ) {
				T model = clazz.newInstance();
				
				for (int i=1; i<=col; ++i) {
					// ormmap.set时已经变为小写
					plot.mapping(rsmd.getColumnLabel(i), i, rs, model);
				}
				brs.add(model);
			}
			
		} finally {
			if (rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			plot.stopColnameMapping();
		}
	}
	
	/**
	 * 替换sim格式sql中的$where为指定的where子句
	 */
	private String swapwhere(String simsql, String where) {
		StringBuilder buff = new StringBuilder(where);
		
		if (!where.trim().startsWith("where")) {
			buff.insert(0, "where ");
		}
		buff.insert(0, ' ');
		buff.append(' ');
		
		return simsql.replaceFirst("\\$where", buff.toString());
	}
	
	private void warnning(String msg) {
		System.out.println("警告:(OrmTemplate): " + msg);
	}
	
}
