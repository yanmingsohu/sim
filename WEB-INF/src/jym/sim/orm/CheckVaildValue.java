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
	 * 如果value==null返回false<br>
	 * 如果value!=null但是没有相关的过滤器,则返回true<br>
	 * 否则,如果过滤器返回null则返回false,非null返回true
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean isValid(Object value) {
		if (value!=null) {
			IFilter f = super.getFilter(value.getClass(), false);
			if (f!=null) {
				try {
					return f.see(value) != null;
				} catch (SimFilterException e) {
					e.printStackTrace();
				}
			} else {
				return true;
			}
		}
		return false;
	}
	
}
