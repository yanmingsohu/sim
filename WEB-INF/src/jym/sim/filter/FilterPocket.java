// CatfoOD 2010-8-19 上午08:47:45 yanming-sohu@sohu.com/@qq.com

package jym.sim.filter;

import java.util.HashMap;
import java.util.Map;

import jym.sim.util.Tools;

/**
 * 为过滤器提供统一分配与集成
 */
public class FilterPocket {
	
	/** 该过滤器输出输入的值 */
	public static final IFilter<Object> NULL_FILTER;
	
	private Map<Class<Object>, IFilter<Object>> filters;
	
	
	public FilterPocket() {
		filters = new HashMap<Class<Object>, IFilter<Object>>();
	}
	
	/**
	 * 注册过滤器
	 * 
	 * @param <T> - 被过滤的数据类型
	 * @param type - 被过滤数据类型的class
	 * @param ft - 过滤器，如果T类型的过滤器已经有了，该过滤器会被替代
	 * 
	 * @return 返回T类型原来的过滤器，没有返回null
	 */
	@SuppressWarnings("unchecked")
	public <T> IFilter<T> reg(Class<T> type, IFilter<T> ft) {
		Tools.check(type, "参数type不能为null");
		Tools.check(ft, "参数ft不能为null");
		
		return (IFilter<T>) filters.put((Class<Object>)type, (IFilter<Object>)ft);
	}
	
	/**
	 * 把过滤器加入上一个过滤器的后面
	 * 
	 * @param <T> - 被过滤的数据类型
	 * @param type - 被过滤数据类型的class
	 * @param ft - 过滤器
	 */
	@SuppressWarnings("unchecked")
	public <T> void add(Class<T> type, IFilter<T> ft) {
		IFilter<T> f = (IFilter<T>) filters.get(type);
		if (f!=null) {
			f = new FilterChain<T>(f,ft);
		} else {
			f = ft;
		}
		
		reg(type, f);
	}
	
	/**
	 * 过滤src数据，使用的过滤器通过检查src的类型自动决定，如果src==null
	 * 则返回null,如果找不到该类型的过滤器则返回src
	 * 
	 * @param src - 要过滤的值
	 * @return 过滤后的值
	 * @throws SimFilterException - 过滤器在过滤时抛出的异常
	 */
	public Object filter(Object src) throws SimFilterException {
		if (src!=null) {
			IFilter<Object> f = filters.get(src.getClass());
			
			if (f!=null) {
				return f.see(src);
			}
		}
		
		return src;
	}
	
	/**
	 * 取得指定类型的过滤器
	 * 
	 * @param <T> - 数据类型
	 * @param type - 类型的class
	 * @param getNullFilter - true则如果过滤器类型不存
	 * 在则返回一个不对数据过滤的过滤器，否则返回null
	 * 
	 * @return 对T类型的过滤器
	 */
	@SuppressWarnings("unchecked")
	public <T> IFilter<T> getFilter(Class<T> type, boolean getNullFilter) {
		IFilter<?> f = filters.get(type);
		if (f==null && getNullFilter) {
			f = NULL_FILTER;
		}
		return (IFilter<T>)f;
	}
	
	static {
		NULL_FILTER = new IFilter<Object>() {
			public Object see(Object src) throws SimFilterException {
				return src;
			}
		};
	}
}
