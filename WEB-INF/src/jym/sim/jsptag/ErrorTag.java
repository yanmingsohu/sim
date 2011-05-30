// CatfoOD 2010-5-19 下午12:25:50 yanming-sohu@sohu.com/@qq.com

package jym.sim.jsptag;

import java.io.PrintWriter;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class ErrorTag extends TagSupport {
	
	private static final long serialVersionUID = -2671398119225540784L;
	private Throwable error;
	private String exp;
	private Pattern p;

	@Override
	public int doStartTag() throws JspException {
		initExp();
		
		if (error!=null) {
			JspWriter out = super.pageContext.getOut();
			PrintWriter ps = new PrintWriter(out);
			StackTraceElement[] st = error.getStackTrace();
			
			ps.print(error.getClass());
			ps.print(':');
			ps.print(error.getMessage());
			ps.print("<br/>");
			boolean isSkip = false;
			
			for (int i=0; i<st.length; ++i) {
				if ( p.matcher(st[i].getClassName()).find() ) {
					printErr(st[i], ps);
					isSkip = false;
				} else {
					if (!isSkip) {
						ps.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
						ps.append("~:&nbsp; ...<br/>");
					}
					isSkip = true;
				}
			}
		}
		return super.doStartTag();
	}
	
	private void initExp() {
		if (exp==null) {
			exp = ".*";
		}
		try {
			p = Pattern.compile(exp);
		} catch (PatternSyntaxException e) {
			e.printStackTrace();
			exp = null;
			initExp();
		}
	}
	
	private void printErr(StackTraceElement st, PrintWriter ps) {
		ps.print("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;~:&nbsp; <span class='er_classname'>");
		ps.print(st.getClassName());
		ps.print("</span> - <span class='er_methodname'>");
		ps.print(st.getMethodName());
		ps.print("()</span>&nbsp; [ <span class='er_filename'>");
		ps.print(st.getFileName());
		ps.print("</span> | <span class='er_line'>");
		ps.print(st.getLineNumber());
		ps.print("</span> ]");
		ps.print("<br/>");
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}
}
