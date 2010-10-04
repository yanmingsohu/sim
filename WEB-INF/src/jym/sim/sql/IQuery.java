package jym.sim.sql;

import java.sql.Connection;
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
	 * 执行查询语句,并返回IResultSql.exe()的结果
	 * 
	 * @param rsql - sql语句的执行对象
	 * @return IResultSql.exe()
	 */
	public Object query(IResultSql rsql);
	
	/**
	 * 创建一条到数据库的连接,该连接直接从数据源中取得
	 * 连接使用结束需要手动关闭
	 * 
	 * @throws SQLException
	 */
	public Connection createConnection() throws SQLException;
	
	/**
	 * 注册错误处理器
	 */
	public void regExceptionHandle(IExceptionHandle eh);
	
	/**
	 * 取得当前的JDBC会话(事务),此会话是线程安全的
	 */
	public IJdbcSession getSession() throws SQLException;
}
