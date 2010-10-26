// CatfoOD 2010-7-28 下午02:10:10 yanming-sohu@sohu.com/@qq.com

package jym.sim.json;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Json implements IjSon {

	private final static char QUOTATION_MARKS = '"';
	
	private Map<String,Object> map;
	
	
	public Json() {
		map = new LinkedHashMap<String,Object>();
	}
	
	public void set(Object name, Object value) {
		map.put(formatJson(name.toString() ), value);
	}
	
	public void set(Object name, boolean b) {
		set(name, new Primitive(b));
	}
	
	public void set(Object name, long i) {
		set(name, new Primitive(i));
	}
	
	public void set(Object name, double d) {
		set(name, new Primitive(d));
	}
	
	public void set(Object name, Collection<?> c) {
		set(name, new FromCollection(c));
	}
	
	public void set(Object name, Map<?, ?> map) {
		set(name, new FromMap(map));
	}
	
	public void setBean(Object name, Object bean) {
		set(name, new FromBean(bean));
	}
	
	/**
	 * 设置值，如果result是null数字或布尔，则不使用引号包围
	 * 不在输出时判断是为了兼容之前的代码
	 */
	protected void setOrPrimitive(Object name, Object result) {
		if (result==null 
				|| result.getClass().isPrimitive()
				|| result instanceof Number
				|| result instanceof Boolean ) 
		{
			set(name, new Primitive(result));
		} else {
			set(name, result);
		}
	}

	public IjSon createSub(Object name) {
		IjSon rj = null;
		
		Object obj = map.get(formatJson(name.toString()));
		
		if (obj instanceof IjSon) {
			rj = (IjSon) obj;
		} 
		else if (obj==null) {
			rj = new Json();
			set(name, rj);
		}
		
		return rj;
	}
	
	public void go(Appendable out) throws IOException {
		Iterator<String> it = map.keySet().iterator();
		
		out.append('{');
			while (it.hasNext()) {
				String name = it.next();
				Object value = map.get(name);
				
				out.append(QUOTATION_MARKS).append(name).append(QUOTATION_MARKS);
				out.append(':');
				if (value instanceof IGo) {
					IGo rj = (IGo) value;
					rj.go(out);
				}
				else {
					out.append(QUOTATION_MARKS).append(
							formatJson( String.valueOf(value) )
						).append(QUOTATION_MARKS);
				}
				
				if (it.hasNext()) {
					out.append(',');
				}
			}
		out.append('}');
	}

	protected static String formatJson(String str) {
		return	JSonFormater.frm(str);
	}
	
	public String toString() {
		StringBuilder out = new StringBuilder();
		try {
			go(out);
		} catch (IOException e) {
			out.append(e);
		}
		return out.toString();
	}

}
