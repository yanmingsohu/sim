// CatfoOD 2010-10-15 обнГ12:36:05 yanming-sohu@sohu.com/@qq.com

package jym.sim.jsptag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class IsNullTag extends TagSupport {

	private static final long serialVersionUID = 3149365591295559804L;
	private boolean isnull = true;

	@Override
	public int doStartTag() throws JspException {
		return isnull? EVAL_BODY_INCLUDE: SKIP_BODY;
	}

	public void setObj(String v) {
		Object o = super.pageContext.getRequest().getAttribute(v);
		setRef(o);
	}
	
	public void setRef(Object o) {
		isnull = (o==null);
	}
}
