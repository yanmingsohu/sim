// CatfoOD 2010-1-4 上午09:36:50

package jym.sim.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface IServletData extends ISessionData {

	HttpServletRequest getHttpServletRequest();
	HttpServletResponse getHttpServletResponse();
	
	/**
	 * HttpServletRequest的便捷方法
	 */
	String getParameter(String name);
	
	/**
	 * HttpServletRequest的便捷方法
	 */
	void setAttribute(String name, Object obj);
	
	/**
	 * HttpServletRequest的便捷方法
	 */
	Object getAttribute(String name);
	
	/**
	 * response.getWriter()的便捷方法
	 */
	void print(Object data);
}
