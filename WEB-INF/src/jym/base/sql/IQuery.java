package jym.base.sql;

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
}
