package jym.sim.sql;

import java.sql.SQLException;

/**
 * sql查询接口
 */
public interface IQuery {
	/**
	 * 执行查询
	 * 
	 * @param sql - sql语句的执行对象
	 */
	public void query(ISql sql);
	
	/**
	 * 注册错误处理器
	 */
	public void regExceptionHandle(IExceptionHandle eh);
	
	/**
	 * 取得当前的JDBC会话(事务),此会话是线程安全的
	 */
	public IJdbcSession getSession() throws SQLException;
}
