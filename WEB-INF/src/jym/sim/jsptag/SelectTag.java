// CatfoOD 2010-5-11 下午12:39:24 yanming-sohu@sohu.com/@qq.com

package jym.sim.jsptag;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import jym.sim.util.Tools;

public class SelectTag extends TagSupport {

	private static final long serialVersionUID = 1535321924476706984L;
	
	private Map<String, String> enumMap;
	
	private String errorMsg = null;
	private String sname;
	private String cname;
	private String fname;
	/** 这个值是集合的索引,指明集合中的哪个元素是默认选项 */
	private String value;
	private JspWriter out;
	private boolean empty = false;
	private String def;
	
	
	@SuppressWarnings("unchecked")
	protected void init() {
		out = super.pageContext.getOut();
		
		try {
			Class<?> clazz = Class.forName(cname);
			Field field = clazz.getField(fname);
			Object value = field.get(null);
			
			if (value!=null) {
				if ( value instanceof String[] ) {
					array2map((String[]) value);
				}
				else if ( value instanceof Map<?,?> ) {
					enumMap = (Map<String, String>) value;
				}
				else {
					error(getEnumObjName() 
							+ "属性类型不是有效的枚举类型-(String[], Map)");
				}
			} else {
				error(getEnumObjName() + "属性值为null");
			}
			
		} catch (ClassNotFoundException e) {
			error(cname + "不是有效的类名字");
			
		} catch (SecurityException e) {
			error(getEnumObjName() + "获取属性时错误" + e.getMessage());
			
		} catch (NoSuchFieldException e) {
			error(getEnumObjName() + "不是有效的属性名");
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			
		} catch (IllegalAccessException e) {
			error(getEnumObjName() + "不可访问");
			
		} catch (NullPointerException  e) {
			error(getEnumObjName() + "不是静态字段");
		}
	}
	
	private void array2map(String[] arr) {
		enumMap = new LinkedHashMap<String, String>();
		for (int i=0; i<arr.length; ++i) {
			enumMap.put(Integer.toString(i), arr[i]);
		}
	}
	
	public int doStartTag() throws JspException {
		init();
		
		try {
			out.print("<select name='" + sname + "' ");
			if (def!=null) {
				out.print(def);
			}
			out.print(">");
			
			if (errorMsg==null) {
				printSelect();
			} else {
				printError();
			}
			out.print("</select>");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SKIP_BODY;
	}
	
	private void printSelect() throws IOException {
		if (!empty) {
			out.print("<option selected disabled>请选择</option>");
		}
		
		Iterator<String> it = enumMap.keySet().iterator();
		
		while (it.hasNext()) {
			String k = it.next();
			String v = enumMap.get(k);
			
			out.print("<option ");
			if (k.equals(value)) {
				out.print("selected ");
			}
			out.print("value=");
			out.print("'");
			out.print(k);
			out.print("'>");
			out.print(v);
			out.print("</option>");
		}
	}
	
	private void printError() throws IOException {
		out.print("<option value='NaN'>");
		out.print(errorMsg);
		out.print("</option>");
	}
	
	public void setName(String name) {
		sname = name;
	}
	
	public void setClazz(String name) {
		cname = name;
	}
	
	public void setField(String name) {
		fname = name;
	}
	
	/** 枚举对象域的完整名字 */
	public String getEnumObjName() {
		return cname + '.' + fname;
	}
	
	public void setValue(String v) {
		value = v;
	}
	
	private void error(String s) {
		Tools.pl("SelectTag: " + s);
		errorMsg = s;
	}

	/**
	 * 返回枚举对象,没有返回null
	 */
	public Map<String, String> getEnumMap() {
		return enumMap;
	}
	
	protected void print(Object o) throws IOException {
		out.print(o);
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public void setDef(String def) {
		this.def = def;
	}
}
