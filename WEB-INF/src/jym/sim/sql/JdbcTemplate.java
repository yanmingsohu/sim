// CatfoOD 2010-4-16 上午08:03:19 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jym.sim.util.Tools;
import jym.sim.util.UsedTime;

public class JdbcTemplate implements IQuery, ICall {
	
	private static ThreadLocal<JdbcSession> db_connect = new ThreadLocal<JdbcSession>();

	private static int maxSessCount = 20;
	private static int sessionCount = 1;
	private ThreadLocal<IExceptionHandle> handle;
	private boolean showsql = false;
	private DataSource src;
	
	
	/**
	 * 用数据源初始化模板
	 */
	public JdbcTemplate(DataSource ds) {
		init(ds);
		handle = new ThreadLocal<IExceptionHandle>();
	}
	
	/**
	 * 初始化session,默认的事务是基于线程的
	 */
	protected JdbcSession initSession() throws SQLException {
		JdbcSession js = db_connect.get();
		if (js==null) {
			js = createSessionInstance();
			db_connect.set(js);
		}
		return js;
	}
	
	public IJdbcSession getSession() {
		try {
			return initSession();
		} catch(SQLException e) {
			handleException(e);
		}

		return null;
	}
	
	
	/**
	 * 是否回显sql语句,默认不显示
	 * @param show - true显示
	 */
	public void showSql(boolean show) {
		showsql = show;
	}
	
	public boolean isShowSql() {
		return showsql;
	}
	
	/**
	 * 用上下文中的数据源初始化模板
	 * 
	 * @param name - 要查询的数据源名称,如:<br>
	 * 				<code> "java:/comp/env/jdbc/ora_rmcsh" <code>
	 */
	public JdbcTemplate(String name) throws NamingException {
		InitialContext cxt = new InitialContext();
		Tools.check(cxt, "JdbcTemplate:上下文未定义");
		init( (DataSource) cxt.lookup( name ) );
	}
	
	private void init(DataSource ds) {
		Tools.check(ds, "数据源无效");
		src = ds;
	}

	public void query(final ISql sql) {
		query(new IResultSql() {
			public Object exe(Statement stm) throws Throwable {
				sql.exe(stm);
				return null;
			}
		});
	}
		
	public Object query(IResultSql sql) {
		
		ProxyStatement proxy  = null;
		Statement st = null;
		JdbcSession js = null;
		Object result = null;
		
		try {
			UsedTime.start("执行sql");
			js = initSession();
			Connection conn = js.getConnection();
			st = conn.createStatement();
			proxy = getProxy(st);
			
			result = sql.exe(proxy);
			
			if (showsql) {
				Tools.plsql(proxy.getSql());
				UsedTime.endAndPrint();
			}
		
		} catch (SQLException e) {
			String msg = e.getMessage();
			if (msg==null) {
				msg = "未知的sql异常";
			}
			Tools.p(msg.trim() + ": ");
			
			if (proxy!=null) {
				Tools.plsql(proxy.getSql());
			} else {
				Tools.pl("unknow sql.");
			}
			handleException(e);
			
		} catch (Throwable t) {
			t.printStackTrace();
			handleException(t);
			
		} finally {
			if (st!=null) {
				try {
					st.close();
				} catch (SQLException e) {}
			}
			if (js!=null) {
				js.close();
			}
		}
		
		return result;
	}
	
	public void call(ICallData cd) {
		StringBuilder buff = new StringBuilder();
		JdbcSession js = null;
		CallableStatement cs = null;
		
		try {
			js = initSession();
			Connection conn = js.getConnection();
			
			buff.append('{');
			if (cd.hasReturnValue()) {
				buff.append("?=");
			}
			buff.append(" call ");
			buff.append(cd.getProcedureName());
			
			int parmCount = cd.getParameterCount();
			if (parmCount>0) {
				buff.append(" (");
				for (int i=0; i<parmCount; ++i) {
					buff.append("?");
					if (i<parmCount-1) {
						buff.append(",");
					}
				}
				buff.append(" )");
			}
			buff.append('}');
			
			cs = conn.prepareCall(buff.toString());
			
			cd.exe(cs);
			
		} catch (Throwable t) {
			Tools.pl("存储过程错误:" + buff.toString());
			t.printStackTrace();
			handleException(t);
			
		} finally {
			if (cs!=null) {
				try {
					cs.close();
				} catch (SQLException e) {}
			}
			if (js!=null) {
				js.close();
			}
		}
	}

	public void regExceptionHandle(IExceptionHandle eh) {
		handle.set(eh);
	}
	
	/**
	 * 数据库抛出的异常注入这个方法中，然后由注册过的异常处理器处理
	 */
	protected void handleException(Throwable t) {
		IExceptionHandle ie = handle.get();
		if (ie!=null) {
			ie.exception(t, t.getMessage());
			// t.printStackTrace();
		}
	}
	
	/**
	 * 取得线程唯一的Connection对象,返回的Connection不可以关闭,否则会引起错误
	 * 
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException {
		return initSession().getConnection();
	}
	
	private ProxyStatement getProxy(Statement st) {
		return (ProxyStatement) Proxy.newProxyInstance(
				this.getClass().getClassLoader(), 
					new Class[]{ ProxyStatement.class },
						new StatementHandler(st) );
	}
	
	/**
	 * Statement代理,用于截取Sql
	 */
	public class StatementHandler implements InvocationHandler {
		private Statement statement;
		private String sql;
		
		public StatementHandler(Statement st) {
			statement = st;
		}

		public Object invoke(Object st, Method m, Object[] ps) 
		throws Throwable, SQLException
		{
			String mname = m.getName();

			if (mname.equals("executeQuery")) {
				sql = (String) ps[0];
			}
			else if (mname.equals("executeUpdate")) {
				sql = (String) ps[0];
			}
			else if (mname.equals("execute")) {
				sql = (String) ps[0];
			}
			else if (mname.equals("getSql")) {
				return getSql();
			}
			
			try {
				return m.invoke(statement, ps);
				
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}
		
		private String getSql() {
			return sql;
		}
	}
	
	private interface ProxyStatement extends Statement {
		public String getSql();
	}
	
	protected JdbcSession createSessionInstance() throws SQLException {
		return new JdbcSession();
	}


	/**
	 * 默认自动递交的事务
	 */
	public class JdbcSession implements IJdbcSession {
		private Connection conn;
		
		private JdbcSession() throws SQLException {
			++sessionCount;
			getConnection();
			
			if (sessionCount%maxSessCount==0) {
				Tools.pl(new Date() + "使用的数据库连接: " + sessionCount);
				maxSessCount += 20;
			}
		}
		
		public Connection getConnection() throws SQLException {
			if (conn==null || conn.isClosed()) {
				conn = src.getConnection();
			}
			return conn;
		}

		public boolean commit() {
			try {
				conn.commit();
				return true;
			} catch (SQLException e) {
				handleException(e);
			}
			return false;
		}

		public boolean isAutoCommit() {
			try {
				return conn.getAutoCommit();
			} catch (SQLException e) {
				handleException(e);
			}
			return true;
		}

		public boolean releaseSavepoint(Savepoint savepoint) {
			try {
				conn.releaseSavepoint(savepoint);
				return true;
			} catch (SQLException e) {
				handleException(e);
			}
			return false;
		}

		public boolean rollback() {
			return rollback(null);
		}

		public boolean rollback(Savepoint savepoint) {
			try {
				if (savepoint==null) conn.rollback();
				else conn.rollback(savepoint);
				
				return true;
			} catch (SQLException e) {
				handleException(e);
			}
			return false;
		}

		public void setCommit(boolean isAuto) {
			try {
				conn.setAutoCommit(isAuto);
			} catch (SQLException e) {
				handleException(e);
			}
		}

		public Savepoint setSavepoint() {
			try {
				return conn.setSavepoint();
			} catch (SQLException e) {
				handleException(e);
			}
			return null;
		}

		public Savepoint setSavepoint(String name) {
			try {
				return conn.setSavepoint(name);
			} catch (SQLException e) {
				handleException(e);
			}
			return null;
		}
		
		public void close() {
			if (isAutoCommit()) {
				// 把JdbcSession从线程变量中移除,
				// 直到没有其他引用时再释放连接
				db_connect.set(null);
			}
		}
		
		/**
		 *  线程对象在线程退出后被释放,conn也会被释放,但是因为
		 *  conn是数据池创建的所以仍然有引用,必须手动关闭
		 */
		protected void finalize() throws Throwable {			
			if (conn!=null) {
				conn.close();
				conn = null;
				--sessionCount;
			}
		}
	}

}
