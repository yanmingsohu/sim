// CatfoOD 2011-4-25 上午08:49:39 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.sql.PreparedStatement;

/**
 * sql语句的执行者
 */
public interface IPrepSql {
	
	/**
	 * JdbcTemplate回调函数, 处理中的sql对象都无需关闭
	 * 
	 * @param pstm - JdbcTemplate创建的PreparedStatement 
	 * @throws Throwable - 函数中不需要捕捉任何异常
	 */
	public void exe(PreparedStatement pstm) throws Throwable;
	
	/**
	 * 返回sql语句, 用来初始化PreparedStatement
	 * @see java.sql.PreparedStatement
	 */
	public String getSql();
	
}
