// CatfoOD 2010-6-12 下午04:14:42 yanming-sohu@sohu.com/@qq.com

package jym.sim.sql;

/**
 * 存储过程调用接口
 */
public interface ICall {

	/**
	 * 呼叫数据库存储过程
	 */
	public void call(ICallData cd);
	
}
