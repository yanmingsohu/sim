package jym.sim.sql;


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
	 * 注册错误处理器,eh是基于线程安全的,所以每个线程都需要分别注册<br>
	 * 每个线程只能注册唯一一个处理器,否则前一个处理器会被覆盖
	 */
	public void regExceptionHandle(IExceptionHandle eh);
	
	/**
	 * 取得当前的JDBC会话(事务),此会话默认是线程安全的,<br>
	 * 如果要实现不同的事务方法,则重写此函数<br>
	 * 如果连接已经关闭,则返回null
	 */
	public IJdbcSession getSession();
}
