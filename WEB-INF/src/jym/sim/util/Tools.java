// CatfoOD 2009-10-26 下午09:15:11

package jym.sim.util;

import java.io.PrintStream;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Tools {
	
	public static final String start = 
		">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
	public static final String end = 
		"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n";

	private static volatile int sqlid = 0;
	private static int id = 0;
	private static PrintStream out = System.out;
	
	
	/**
	 * 重新定位输出流,默认System.out;
	 */
	public static void setOut(PrintStream newout) {
		out = newout;
	}
	
	public static void nocatch(HttpServletResponse resp) {
		resp.setHeader("cache-control", "no-cache");
	}
	
	public static void setAjaxXmlHeader(HttpServletResponse resp) {
		nocatch(resp);
		resp.setContentType("text/xml");
	}
	
	public static String creatTagID(String tagname) {
		int _id;
		synchronized (Tools.class) {
			_id = id++;
		}
		return tagname + _id;
	}
	
	public static boolean isNull(String s) {
		return s==null || s.trim().length()==0;
	}

	/**
	 * 返回name的第一个字符
	 */
	public static String getFirstName(String name) {
		if (name.length()>0) {
			return name.substring(0, 1);
		}
		return name;
	}
	
	public static void pl(Object o) {
		out.println(o);
	}
	
	public static void p(Object o) {
		out.print(o);
	}
	
	public static void plsql(String sql) {
		out.println( String.format("sql(%1$#06x): %2$s", sqlid++, sql) );
	}
	
	public static void pl(Object ...o) {
		for (int i=0; i<o.length; ++i) {
			out.print(o[i]+" ");
		}
		out.println();
	}
	
	public static void plerr(Throwable t) {
		StringBuilder ps = new StringBuilder();
		StackTraceElement[] st = t.getStackTrace();
		
		ps.append(t.getClass());
		ps.append(':');
		ps.append(t.getMessage());
		ps.append("\n");
		
		for (int i=0; i<st.length; ++i) {
			ps.append("\t~: ");
			ps.append(st[i].getClassName());
			ps.append(" - ");
			ps.append(st[i].getMethodName());
			ps.append("() [ ");
			ps.append(st[i].getFileName());
			ps.append(" | ");
			ps.append(st[i].getLineNumber());
			ps.append(" ]\n");
		}
		
		pl(ps);
	}
	
	/**
	 * 如果o==null, 则抛出异常,异常信息在msg中定义
	 * 
	 * @throws RuntimeException
	 */
	public static void check(Object o, String msg) {
		if (o==null) {
			throw new RuntimeException(msg);
		}
	}
	
	/**
	 * 复制HttpServletRequest中的请求参数到属性列表
	 * 以便jsp的EL调用
	 */
	public static void copyParam2Attrib(HttpServletRequest req) {
		Enumeration<?> e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			Object value= req.getParameter(name);
			req.setAttribute(name, value);
		}
	}
	
	public static void start() {
		pl(start);
	}
	
	public static void end() {
		pl(end);
	}
}
