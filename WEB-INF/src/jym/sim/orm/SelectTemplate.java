package jym.sim.orm;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import jym.sim.sql.IQuery;
import jym.sim.sql.ISql;
import jym.sim.sql.JdbcTemplate;
import jym.sim.sql.Logic;
import jym.sim.util.BeanUtil;
import jym.sim.util.Tools;

/**
 * 数据库实体检索模板
 */
public class SelectTemplate<T> extends JdbcTemplate implements ISelecter<T>, IQuery {

	private Class<T> clazz;
	private IOrm<T> orm;
	private Plot<T> plot;
	
	private final static String SELECT = "select * from ";
	
	
	/**
	 * jdbc模板构造函数, 全部使用表格名映射实体属性
	 * 
	 * @param ds - 数据源
	 * @param modelclass - 数据模型的class类
	 * @param tablename - 数据库表名
	 * @param priKey - 主键名
	 */
	public SelectTemplate(DataSource ds, final Class<T> modelclass, 
			final String tablename, final String priKey) {
		
		this(ds, new IOrm<T>() {

			public Class<T> getModelClass() {
				return modelclass;
			}

			public void mapping(IPlot plot) {
			}

			public String getTableName() {
				return tablename;
			}

			public String getKey() {
				return priKey;
			}
		});
	}
	
	/**
	 * jdbc模板构造函数,默认每次连接不会自动关闭连接
	 * 
	 * @param orm - 数据库列数据与bean实体属性映射策略
	 * @throws SQLException - 数据库错误抛出异常
	 */
	public SelectTemplate(DataSource ds, IOrm<T> orm) {
		super(ds);
		this.orm = orm;
		
		check();
		init();
	}
	
	private void init() {
		clazz = orm.getModelClass();
		plot = new Plot<T>(orm);
	}
	
	private void check() {
		Tools.check(orm.getKey(), 			"getKey()不能返回null");
		Tools.check(orm.getModelClass(),	"getModelClass()不能返回null");
		Tools.check(orm.getTableName(),		"getTableName()不能返回null");
	}
	
	protected void loopMethod2Colume(T model, IColumnValue cv) {
		Method[] ms = plot.getAllMethod();
		
		for (int i=0; i<ms.length; ++i) {
			String colname = plot.getColname(ms[i]);
		
			if (colname!=null) {
				try {
					Object value = ms[i].invoke(model, new Object[0]);
					
					if ( BeanUtil.isValid(value) ) {
						cv.set(colname, value);
					}
					
				} catch (Exception e) {
					warnning("invoke错误: "+ e);
				}
			}
		}
	}
	
	public List<T> select(final T model, final String join) {
		
		final StringBuilder selectsql = new StringBuilder(SELECT);
		selectsql.append(orm.getTableName());
		
		loopMethod2Colume(model, new IColumnValue() {
			
			boolean first = true;

			public void set(String column, Object value) {
				if ( BeanUtil.isValid(value) ) {
					if (first) {
						selectsql.append(" where ");
						first = false;
					} else {
						selectsql.append(join);
					}
					
					Logic logic = plot.getColumnLogic(column);
					
					selectsql.append( " (" ).append(column).append(' ')
							.append(logic.in(value)).append( ") " );
				}
			}
			
		});
		
		return select(selectsql.toString());
	}
	
	private List<T> select(final String sql) {
		final List<T> brs = new ArrayList<T>();
		
		query(new ISql() {
			public void exe(Statement stm) throws Throwable {
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
	
	private void warnning(String msg) {
		System.out.println("警告:(SelectTemplate): " + msg);
	}

	public Class<T> getModelClass() {
		return orm.getModelClass();
	}
	
	protected IOrm<T> getOrm() {
		return orm;
	}
	
	protected Plot<T> getPlot() {
		return plot;
	}
	
}
