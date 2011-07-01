// CatfoOD 2010-8-13 下午02:09:49 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

/**
 * 处理事务的对象（句柄）
 */
public interface ITransactionHanle {
	
	/**
	 * 事务的处理在该方法中进行,如果抛出异常则整个事务会回滚<br>
	 * 事务中不能启动线程, 否则会逃逸事务
	 */
	void start() throws Exception;

}
