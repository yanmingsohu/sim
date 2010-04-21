// CatfoOD 2010-1-4 上午09:56:18

package jym.sim.base;

import jym.sim.util.IServletData;

public interface IHttpData extends IServletData {
	/**
	 * 返回：
	 * web.xml配置中beanclass属性的对象，使用post/get参数初始化
	 */
	Object getFormObj();
}
