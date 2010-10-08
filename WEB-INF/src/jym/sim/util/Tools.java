// CatfoOD 2009-10-26 下午09:15:11

package jym.sim.util;

import java.awt.Toolkit;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Tools {
	
	public static final String start 
		= ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
	public static final String end 
		= "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n";

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
	
	/**
	 * 打印错误,不过滤输出
	 * @param t - 要打印的异常
	 */
	public static void plerr(Throwable t) {
		plerr(t, null);
	}
	
	/**
	 * 打印错误并过滤输出
	 * @param t - 要打印的错误
	 * @param filterExp - 正则表达式,用表达式过滤类名,
	 * 			符合则打印,否则忽略 如果表达式错误则匹配全部
	 */
	public static void plerr(Throwable t, String filterExp) {
		StringBuilder ps = new StringBuilder();
		StackTraceElement[] st = t.getStackTrace();
		
		Pattern p = null; 
		try {
			if (filterExp!=null) {
			p = Pattern.compile(filterExp);
			}
		} catch(Exception e) {}
		
		ps.append(t.getClass());
		ps.append(':');
		ps.append(t.getMessage());
		ps.append("\n");
		
		boolean isSkip = false;
		for (int i=0; i<st.length; ++i) {
			if ( p==null ||  p.matcher(st[i].getClassName()).find() ) {
				ps.append("\t~: ");
				ps.append(st[i].getClassName());
				ps.append(" - ");
				ps.append(st[i].getMethodName());
				ps.append("() [ ");
				ps.append(st[i].getFileName());
				ps.append(" | ");
				ps.append(st[i].getLineNumber());
				ps.append(" ]\n");
				isSkip = false;
			} else {
				if (!isSkip) {
					ps.append("\t~: ... [ overlook ]\n");
				}
				isSkip = true;
			}
		}
		
		pl(ps);
	}
	
	/**
	 * 打印当前过程的调用堆栈
	 */
	public static void plStackTrace() {
		Tools.plerr(new Throwable("调用堆栈"));
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
	
	/**
	 * 发出一个音频嘟嘟声
	 */
	public static void beep() {
		Toolkit.getDefaultToolkit().beep();
	}
	
	/**
	 * 复制指定的数组，截取或用 0 填充（如有必要），以使副本具有指定的长度。
	 * 对于在原数组和副本中都有效的所有索引，这两个数组将包含相同的值。
	 * 对于在副本中有效而在原数组无效的所有索引，副本将包含 (byte)0。
	 * 当且仅当指定长度大于原数组的长度时，这些索引存在。<br>
	 * <br>
	 * 此方法为java1.5提供便捷的数组复制
	 */
	public static byte[] copyOf(byte[] original, int newLength) {
		byte[] newarr = new byte[newLength];
		System.arraycopy(original, 0, newarr, 0, original.length);
		
		return newarr;
	}
	
	/**
	 * 替换ch数组中所有的s字符为r字符
	 */
	public static void replaceAll(char[] ch, char s, char r) {
		for (int i=0; i<ch.length; ++i) {
			if (ch[i]==s) {
				ch[i] = r;
			}
		}
	}
	
}
