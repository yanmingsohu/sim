// CatfoOD 2010-6-12 上午08:00:29 yanming-sohu@sohu.com/@qq.com

package jym.sim.tags;

import java.io.PrintWriter;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import jym.sim.css.InnerCss;

public class ErrorTag extends HtmlTagBase {
	
	public final static String DEFAULT_EXP = ".*";
	
	private Throwable error;
	private String exp;
	private Pattern p;
	

	public ErrorTag(Throwable error) {
		this(error, null);
	}
	
	public ErrorTag(Throwable error, String exp) {
		super("span");
		this.error = error;
		this.exp = exp;
		bandCss();
		doStartTag();
	}
	
	private void initExp() {
		if (exp==null) {
			exp = DEFAULT_EXP;
		}
		try {
			p = Pattern.compile(exp);
		} catch (PatternSyntaxException e) {
			e.printStackTrace();
			exp = null;
			initExp();
		}
	}
	
	private void bandCss() {
		InnerCss css = new InnerCss("/jym/sim/tags/error_tag.css");
		append(css);
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
	
	private void doStartTag() {
		initExp();
		this.addAttribute("class", "er_css");
		
		if (error!=null) {
			StackTraceElement[] st = error.getStackTrace();
			
			PrintWriter ps = this.createText();
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
	}
	
	public static void main(String []s) {
		Exception e = new Exception();
		ErrorTag et = new ErrorTag(e);
		PrintWriter pw = new PrintWriter(System.out);
		et.printout(pw);
		pw.close();
	}
}
