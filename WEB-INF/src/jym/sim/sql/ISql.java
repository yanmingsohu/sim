// CatfoOD 2010-4-16 上午08:22:29 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.sql.Statement;

/**
 * sql语句的执行者
 */
public interface ISql {
	
	/**
	 * JdbcTemplate回调函数, 处理中的sql对象都无需关闭
	 * 
	 * @param stm - JdbcTemplate创建的Statement 
	 * @throws Throwable - 函数中不需要捕捉任何异常
	 */
	public void exe(Statement stm) throws Throwable;
	
}
