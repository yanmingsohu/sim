package jym.sim.jsptag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class NotnullTag extends TagSupport {
	
	private static final long serialVersionUID = 3149365591295559804L;
	private boolean notnull = true;

	@Override
	public int doStartTag() throws JspException {
		return notnull? EVAL_BODY_INCLUDE: SKIP_BODY;
	}

	public void setObj(String v) {
		Object o = super.pageContext.getRequest().getAttribute(v);
		setRef(o);
//		if (notnull) { // 测试集合是否为空的方�?
//			if (o instanceof Collection<?>) {
//				Collection<?> col = (Collection<?>) o;
//				notnull = col.size()>0;
//			}
//		}
	}
	
	public void setRef(Object o) {
		notnull = (o!=null);
	}
}
