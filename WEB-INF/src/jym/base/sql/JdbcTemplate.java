// CatfoOD 2010-4-16 上午08:03:19 yanming-sohu@sohu.com/@qq.com

package jym.base.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jym.base.util.Tools;

public class JdbcTemplate implements IQuery {
	
	private DataSource src;
	
	/**
	 * 用数据源初始化模板
	 */
	public JdbcTemplate(DataSource ds) {
		init(ds);
	}
	
	/**
	 * 用上下文中的数据源初始化模板
	 * 
	 * @param name - 要查询的数据源名称,如:<br>
	 * 				<code> java:/comp/env/jdbc/ora_rmcsh <code>
	 */
	public JdbcTemplate(String name) throws NamingException {
		InitialContext cxt = new InitialContext();
		Tools.check(cxt, "上下文未定义");
		init( (DataSource) cxt.lookup( name ) );
	}
	
	private void init(DataSource ds) {
		Tools.check(ds, "数据源无效");
		src = ds;
	}

	public void query(ISql sql) {
		Connection conn = null;
		Statement st = null;
		
		try {
			conn = src.getConnection();
			st = conn.createStatement();
			sql.exe(st);
			
		} catch (Throwable t) {
			sql.exception(t, t.getMessage());
			t.printStackTrace();
			
		} finally {
			if (st!=null) {
				try {
					st.close();
				} catch (SQLException e) {}
			}
			if (conn!=null) {
				try {
					conn.close();
				} catch (Exception e) {};
			}
		}
	}
}
