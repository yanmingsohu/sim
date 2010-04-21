// CatfoOD 2009-12-30 下午12:44:05

package jym.sim.base;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jym.sim.util.ForwardProcess;
import jym.sim.util.ICallBack;
import jym.sim.util.ServletData;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 对Struts1.3 Action 类的封装
 * 实现类无需使用execute方法复杂的参数
 */
public abstract class ActionBase extends Action {
	
	/**
	 * 子类不要覆盖这个方法,ActionBase把Struts1的execute方法进行了封装<br>
	 * 执行方式依照execute(IActionData)方法的注释实现
	 */
	@Override
	public final ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ActionData ad = new ActionData(mapping, form, request, response);
		prepositive(ad);
		Object obj = execute(ad);
		
		ForwardCallBack fcb = new ForwardCallBack(mapping, obj);
		ForwardProcess.exec(ad, obj, fcb);
		
		return fcb.getForward();
	}
	
	/**
	 * 在execute方法执行前，提供插入操作的机会，默认什么都不做
	 * 
	 * @param data - Action参数的封装
	 */
	protected void prepositive(IActionData data) throws Exception {
	}
	
	/**
	 * 被Action的execute方法包装，以简化原execute的参数依赖
	 * 
	 * @param data - Action参数的封装
	 * @return	如果返回String类型，则String为有效的mapping路径<br>
	 * 			如果返回IPrinter类型，则打印他，并返回null路径<br>
	 * 			如果返回其他类型，则直接把toString的结果输出到客户端，
	 * 			并返回null路径<br>
	 * 
	 * @throws Exception
	 */
	abstract public Object execute(IActionData data) throws Exception ;
	
	/**
	 * 传递给execute
	 */
	private class ActionData extends ServletData implements IActionData {
		
		private ActionMapping map;
		private ActionForm fm;
		
		private ActionData(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) 
		throws IOException
		{
			super(request, response);
			map = mapping;
			fm  = form;
		}
		
		public ActionForm getActionForm() {
			return fm;
		}

		public ActionMapping getActionMapping() {
			return map;
		}		
	}
	
	private class ForwardCallBack implements ICallBack {
		private Object obj;
		private ActionForward forward;
		private ActionMapping mapping;
		
		private ForwardCallBack(ActionMapping map, Object ob) {
			obj = ob;
			mapping = map;
			forward = null;
		}
		
		public void back() throws Exception {
			forward = mapping.findForward( (String)obj );
		}
		
		public ActionForward getForward() {
			return forward;
		}
	}
}
