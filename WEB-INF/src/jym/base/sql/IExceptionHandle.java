// CatfoOD 2010-4-20 上午08:24:37 yanming-sohu@sohu.com/@qq.com

package jym.base.sql;


public interface IExceptionHandle {

	/**
	 * 如果exe()抛出异常这个方法被执行
	 *  
	 * @param tr - 抛出的异常
	 * @param msg - 异常信息
	 */
	public void exception(Throwable tr, String msg);
	
}
