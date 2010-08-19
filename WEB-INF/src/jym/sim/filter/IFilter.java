// CatfoOD 2010-8-19 上午08:32:40 yanming-sohu@sohu.com/@qq.com

package jym.sim.filter;

/**
 * 所有过滤器的父接口父,泛型指示该过滤器过滤的数据类型
 */
public interface IFilter<T> {
	/**
	 * 过滤T类型数
	 * 
	 * @param src - 源数据
	 * @throws SimFilterException - 如果抛出该异常具体的实现不同,处理方式不同
	 * @return 过滤后的数据
	 */
	public T see(T src) throws SimFilterException;
}
