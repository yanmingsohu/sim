// CatfoOD 2009-12-15 下午08:10:04

package jym.sim.base;

import jym.sim.util.IServletData;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * 封装Struts.Action类execute方法的参数
 */
public interface IActionData extends IServletData {
	ActionMapping getActionMapping();
	ActionForm getActionForm();
}
