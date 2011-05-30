// CatfoOD 2009-12-21 下午08:45:13

package jym.sim.css;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jym.sim.tags.IPrinter;

/**
 * 样式表中的一个样式
 */
public class Css implements IPrinter {
	
	private final static String ST = "{";
	private final static String ED = "}";
	private final static String AT = ";";
	private final static String TO = ":";
	
	private String sename;
	private Map<String, String> smap;
	
	/**
	 * 新建一个css样式
	 * @param selecterName - css选择器
	 */
	public Css(String selecterName) {
		sename = selecterName;
		smap = new HashMap<String, String>();
	}
	
	/**
	 * 向样式表中添加一个样式属性
	 * 
	 * @param name - 属性名字
	 * @param value - 属性值（无需;结束）
	 * @return
	 */
	public Css addStyle(String name, String value) {
		smap.put(name, value);
		return this;
	}

	public void printout(PrintWriter out) {
		out.print(sename);
		out.print(ST);
		
		Iterator<String> names = smap.keySet().iterator();
		while (names.hasNext()) {
			String name = names.next();
			out.print(name);
			out.print(TO);
			out.print(smap.get(name));
			out.print(AT);
		}
		out.println(ED);
	}
}
