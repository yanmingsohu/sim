// CatfoOD 2009-12-15 下午08:10:04

package jym.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jym.base.util.ISessionData;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * 封装Struts.Action类execute方法的参数
 */
public interface IActionData extends ISessionData {
	
	ActionMapping getActionMapping();
	ActionForm getActionForm();
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
	 * HttpSession的便捷方法
	 */
	void setSessionAttribute(String name, Object obj);
	
	/**
	 * HttpSession的便捷方法
	 */
	Object getSessionAttribute(String name);
	
	/**
	 * response.getWriter()的便捷方法
	 */
	void print(Object data);
}
