// CatfoOD 2010-11-10 上午10:14:48 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

/**
 * 在拼装update语句时,属性如何转换为字符串,如果未指定则使用全局设置
 * 如果给列设置了该接口的实现,则全局设定无效
 */
public interface IUpdateLogic extends ISqlLogic {
	
	/**
	 * 如果up方法返回该值,则允许把列值设置为null<br>
	 * 如果要设置为null值,则up方法必须返回这个属性而非"null"字符串
	 */
	String NULL = "null";
	
	/**
	 * 拼装为update sql的策略
	 * 
	 * @param columnValue - 实体属性中的值
	 * @return 只有非null值该列才会被拼装为sql,
	 * 	如果要设置为null需要返回该接口的静态属性NULL
	 */
	Object up(Object columnValue);
	
}
