package jym.base.orm;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * 数据库实体映射模板
 */
public class JdbcTemplate<T> implements ISelecter<T> {
	
	private DataSource dsrc;
	private Connection conn;
	private String sql;
	private PreparedStatement ps;
	private IOrm<T> orm;
	private Class<T> clazz;
	private boolean usecolnamemap = true;
	private boolean autocloseconn = false;
	private Plot plot;
	
	/** 使用小写比较String */
	private Map<String, MethodMapping> ormmap;
	/** 使用小写比较String */
	private Map<String, Method> classMethodmap;
	
	/**
	 * jdbc模板构造函数, autoClose()发方法无效, 连接不会被关闭
	 * 
	 * @param conn - 数据库连接,autoClose()方法无效
	 * @param orm - 数据库列数据与bean实体属性映射策略
	 * @throws SQLException - 数据库错误抛出异常
	 */
	public JdbcTemplate(Connection conn, IOrm<T> orm) throws SQLException {
		this.conn = conn;
		this.orm = orm;
		dsrc = null;
		
		createConn();
		init();
	}
	
	/**
	 * jdbc模板构造函数,默认每次连接不会自动关闭连接
	 * 
	 * @param ds - 数据源
	 * @param orm - 数据库列数据与bean实体属性映射策略
	 * @throws SQLException - 数据库错误抛出异常
	 */
	public JdbcTemplate(DataSource ds, IOrm<T> orm) {
		dsrc = ds;
		this.orm = orm;
		
		init();
	}
	
	private void init() {
		clazz = orm.getModelClass();
		sql = orm.getPrepareSql();
		plot = new Plot();
		
		initMethods();
		initOrm();
	}
	
	/**
	 * jdbc模板构造函数, 全部使用表格名映射实体属性
	 * 
	 * @param conn - 数据库连接
	 * @param modelclass - 数据模型的class类
	 * @param preSql - 查询语句
	 * @throws SQLException - 数据库错误抛出异常
	 */
	public JdbcTemplate(Connection conn, final Class<T> modelclass, final String preSql) 
	throws SQLException {
		
		this(conn, new IOrm<T>() {
			public Class<T> getModelClass() {
				return modelclass;
			}
			public String getPrepareSql() {
				return preSql;
			}
			public void mapping(IPlot plot) {
			}
		});
	}
	
	/**
	 * jdbc模板构造函数, 全部使用表格名映射实体属性
	 * 
	 * @param ds - 数据源
	 * @param modelclass - 数据模型的class类
	 * @param preSql - 查询语句
	 */
	public JdbcTemplate(DataSource ds, final Class<T> modelclass, final String preSql) {
		this(ds, new IOrm<T>() {

			public Class<T> getModelClass() {
				return modelclass;
			}

			public String getPrepareSql() {
				return preSql;
			}

			public void mapping(IPlot plot) {
			}
		});
	}
	
	private void initMethods() {
		Method[] ms = clazz.getMethods();
		classMethodmap = new HashMap<String, Method>();
		for (int i=0; i<ms.length; ++i) {
			// 使用小写比较
			classMethodmap.put(ms[i].getName().toLowerCase(), ms[i]);
		}
	}
	
	private void initOrm() {
		ormmap = new HashMap<String, MethodMapping>();
		orm.mapping(plot);
	}

	private void createConn() throws SQLException {
		if (conn==null || conn.isClosed()) { //  || !conn.isValid(6)
			if (dsrc!=null) {
				conn = dsrc.getConnection();
				ps = conn.prepareStatement(sql);
			} else {
				throw new SQLException("与数据库的连接已经丢失");
			}
		}
	}
	
	public List<T> select(Object ...params) {
		List<T> brs = new ArrayList<T>();
		ResultSet rs = null;
		
		try {
			createConn();
			for (int i=1; i<=params.length; ++i) {
				ps.setObject(i, params[i-1]);
			}
			
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int col = rsmd.getColumnCount();
			
			while ( rs.next() ) {
				T model = clazz.newInstance();
				
				for (int i=1; i<=col; ++i) {
					// ormmap.set时已经变为小写
					mapping(rsmd.getColumnLabel(i), i, rs, model);
				}

				brs.add(model);
			}
			
		} catch(Exception e) {
			warnning("select错误: " + e.getMessage());
			
		} catch(Throwable t) {
			warnning("严重错误: " + t);
			
		} finally {
			if (rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			// 如果dsrc!=null可以从数据源从新获取连接,则可以释放连接
			if (dsrc!=null && autocloseconn) {
				close();
			}
			usecolnamemap = false;
		}
		
		return brs;
	}
	
	private void mapping(String colname, int colc, ResultSet rs, T model) {
		colname = colname.toLowerCase();
		MethodMapping md = null;

		// 自动使用表格列名进行映射
		if (usecolnamemap && !ormmap.containsKey(colname)) {
			md = setMappingPlot(colname, colname, null);
		} else {
			md = ormmap.get(colname);
		}
		
		if (md!=null) {
			try {
				md.invoke(rs, colc, model);
				
			} catch(Exception e) {
				warnning("执行方法 (" + md.getName() + ") 时错误: " + e.getMessage());
			}
		} else {
			warnning(colname+" 指定的数据行没有映射");
		}
	}
	
	
	/**
	 * 如果filedname的类型不是简单类型,则使用sql创建<br>
	 * sql可以为null
	 */
	private MethodMapping setMappingPlot(String filedname, String colname, ISelecter is) {
		String methodName = getSetterName(filedname);
		Method m = getSetterMethod(methodName);
		MethodMapping mm = null;
		
		try {
			mm = new MethodMapping(m, is);
			// ormmap.set 的参数变为小写
			ormmap.put(colname.toLowerCase(), mm);
		} catch (Exception e) {
			warnning("方法(" + m.getName() + ")无效: " + e.getMessage());
		}
		
		return mm;
	}
	
	private String getSetterName(String fieldname) {
		char[] fns = fieldname.toCharArray();
		StringBuilder buff = new StringBuilder();
		buff.append("set");
		buff.append( Character.toTitleCase(fns[0]) );
		buff.append(fns, 1, fns.length-1);
		return buff.toString();
	}
	
	private Method getSetterMethod(String methodname) {
		return classMethodmap.get(methodname.toLowerCase());
	}

	/**
	 * 关闭Connection连接,同时PreparedStatement也被关闭,<br>
	 * 对于频繁执行数据库检索的操作不应给关闭连接<br>
	 * <b>如果未使用数据源构造函数,关闭后本对象不能继续使用</b>
	 */
	public void close() {
		try {
			if (conn!=null)
				conn.close();
		} catch (SQLException e) {
		}
		conn = null;
	}
	
	/**
	 * 每次查询执行完毕后是否自动关闭连接<br>
	 * 如果设置为true则PreparedStatement优化无效<br>
	 * 默认每次连接不会自动关闭连接<br>
	 * <br>
	 * <b>对于频繁执行数据库检索的操作不应给关闭连接</b>
	 */
	public void autoClose(boolean ac) {
		autocloseconn = ac;
	}
	
	private class Plot implements IPlot {
		public void fieldPlot(String fn, String cn) {
			setMappingPlot(fn, cn, null);
		}

		@Override
		public void fieldPlot(String fieldName, String colname, ISelecter getter) {
			//throw new UnsupportedOperationException("暂不支持这个操作");
			setMappingPlot(fieldName, colname, getter);
		}
	}
	
	interface ITransition {
		Object trans(ResultSet rs, int col) throws SQLException;
	}
	
	private void warnning(String msg) {
		System.out.println("警告:(JdbcTemplate): " + msg);
	}
	
}
