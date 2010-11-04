// CatfoOD 2010-9-1 上午10:11:26 yanming-sohu@sohu.com/@qq.com

package jym.sim.orm;

import jym.sim.filter.FilterPocket;
import jym.sim.filter.IFilter;
import jym.sim.filter.SimFilterException;

/**
 * 输入值有效性检查
 */
public class CheckVaildValue extends FilterPocket {
	
	/**
	 * <strike>如果value==null返回false<br>
	 * 如果value!=null但是没有相关的过滤器,则返回true 否则,</strike>
	 * 
	 * 如果没有相关过滤器但值为null则返回false, 否则返回true(即默认不能为null)<br>
	 * 如果有过滤器则,过滤器返回null则返回false,非null返回true
	 */
	@SuppressWarnings({ "unchecked" })
	public boolean isValid(Object value, Class<?> valueType) {
		IFilter f = super.getFilter(valueType, false);
		if (f!=null) {
			try {
				return f.see(value) != null;
			} catch (SimFilterException e) {
				e.printStackTrace();
			}
		} else {
			return value!=null;
		}
		return false;
	}
	
}
