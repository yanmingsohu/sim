// CatfoOD 2010-7-12 下午01:06:50 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.sql.Statement;

/**
 * 返回结果的sql语句执行者
 */
public interface IResultSql {

	/**
	 * JdbcTemplate回调函数, 处理中的sql对象都无需关闭
	 * 
	 * @param stm - JdbcTemplate创建的Statement 
	 * @throws Throwable - 函数中不需要捕捉任何异常
	 * @return 返回一个对象
	 */
	public Object exe(Statement stm) throws Throwable;
	
}
