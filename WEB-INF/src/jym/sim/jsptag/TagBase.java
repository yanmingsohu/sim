// CatfoOD 2011-7-27 上午10:13:39 yanming-sohu@sohu.com/@qq.com

package jym.sim.jsptag;

import javax.servlet.jsp.tagext.TagSupport;

import jym.sim.util.Tools;


public abstract class TagBase extends TagSupport {

	private static final long serialVersionUID = -212267052564379012L;

	
	/** 在控制台上打印错误消息 */
	protected void error(String s) {
		Tools.pl(getClass().getName() + ": (" 
				+ super.pageContext.getPage() + ") " + s);
	}
}
