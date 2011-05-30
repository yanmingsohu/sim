// CatfoOD 2010-8-19 上午09:44:30 yanming-sohu@sohu.com/@qq.com

package jym.sim.filter;

import jym.sim.util.Tools;

/**
 * 多个互相嵌套形成过滤器链
 */
public class FilterChain<T> implements IFilter<T> {
	
	private IFilter<T> f1;
	private IFilter<T> f2;
	
	/**
	 * 过滤器one先过滤,然后是two
	 */
	public FilterChain(IFilter<T> one, IFilter<T> two) {
		Tools.check(one, "one过滤器不能为null");
		Tools.check(two, "two过滤器不能为null");
		
		f1 = one;
		f2 = two;
	}

	public T see(T src) throws SimFilterException {
		return f2.see(f1.see(src));
	}

}
