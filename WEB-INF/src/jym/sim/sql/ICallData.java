// CatfoOD 2010-6-13 下午02:33:03 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

import java.sql.CallableStatement;

public interface ICallData {
	
	/**
	 * 返回存储过程的名字
	 */
	public String getProcedureName();
	
	/**
	 * 返回存储过程参数的个数
	 */
	public int getParameterCount();
	
	/**
	 * 是否使用存储过程的返回值,true产生如下代码:
	 * {?= call <procedure-name>[(<arg1>,<arg2>, ...)]}
	 */
	public boolean hasReturnValue();
	
	/**
	 * 向CallableStatement压入参数,执行过程调用,返回参数<br/>
	 * CallableStatement抛出的异常如果不关心可以不用catch
	 */
	public void exe(CallableStatement cs) throws Exception;
}
