// CatfoOD 2009-12-15 下午08:10:04

package jym.sim.util;

/**
 * servlet session数据对象
 */
public interface ISessionData {
	/**
	 * HttpSession的便捷方法
	 */
	void setSessionAttribute(String name, Object obj);
	
	/**
	 * HttpSession的便捷方法
	 */
	Object getSessionAttribute(String name);
	
}
