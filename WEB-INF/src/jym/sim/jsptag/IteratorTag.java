package jym.sim.jsptag;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import jym.sim.util.Tools;

public class IteratorTag extends TagSupport {
	
	private static final long serialVersionUID = -6643507593386688055L;
	/**
	 * 在Action中把查询的错误信息使用这个name放入request.attribute中<br>
	 * 确保这个标签被执行,则错误的信息会打印到页面上,<b>ActionTemplate已经自动实现</b>
	 */
	public static final String SEARCH_ERROR = "iterator.tag.SEARCH_ERROR";
	
	private Iterator<?> itr;
	private String prefix;
	private boolean nomsg = false;
	private Integer skip;
	

	@Override
	public int doAfterBody() throws JspException {
		return next()? EVAL_BODY_AGAIN: SKIP_BODY;
	}

	@Override
	public int doStartTag() throws JspException {
		if (!itr.hasNext()) {
			printNoItem();
		}

		if (skip!=null && skip>0) {
			for (int i=skip; i>0; --i) {
				if (!itr.hasNext()) break;
				// skip element
				itr.next();
			}
		}
		
		return next()? EVAL_BODY_INCLUDE: SKIP_BODY;
	}
	
	private boolean next() {
		boolean hasnext = itr.hasNext();
		if (hasnext) {
			pageContext.getRequest().setAttribute(prefix, itr.next());
		}
		return hasnext;
	}

	public void setPrefix(String v) {
		prefix = v;
	}
	
	public void setList(String v) {
		Collection<?> list = (Collection<?>) pageContext.getRequest().getAttribute(v);
		if (list==null) {
			error("'" + v + "' 属性==null.");
		}
		setListobj(list);
	}
	
	public void setListobj(Collection<?> list) {
		if (list!=null) {
			itr = list.iterator();
		} else {
			error("list参数为null.");
			itr = new Iterator<Object>() {
				public boolean hasNext() {
					return false;
				}
				public Object next() {
					return null;
				}
				public void remove() {
				}
			};
		}
	}
	
	private void printNoItem() {
		if (nomsg) return;
		
		try {
			String err = null;
			Throwable e = (Throwable) pageContext.getRequest().getAttribute(SEARCH_ERROR);
			if (e!=null) {
				err = e.getMessage();
			} else {
				err = "没有符合条件的记录";
			}
			
			JspWriter out = super.pageContext.getOut();
			out.print(" <span class='trips'>" + err + "</span>");
			
		} catch (IOException e) {
			error(e.getMessage());
		}
	}
	
	private void error(String s) {
		Tools.pl("IteratorTag warn : " + s);
	}

	public void setSkip(Integer skip) {
		this.skip = skip;
	}

	public void setNomsg(boolean nomsg) {
		this.nomsg = nomsg;
	}
}
