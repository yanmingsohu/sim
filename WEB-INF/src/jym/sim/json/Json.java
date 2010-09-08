// CatfoOD 2010-7-28 ÏÂÎç02:10:10 yanming-sohu@sohu.com/@qq.com

package jym.sim.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Json implements IjSon {

	private final static char QUOTATION_MARKS = '"';
	
	private Map<String,Object> map;
	
	
	public Json() {
		map = new HashMap<String,Object>();
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

	public IjSon createSub(Object name) {
		Json rj = null;
		
		Object obj = map.get(formatJson(name.toString()));
		
		if (obj instanceof Json) {
			rj = (Json) obj;
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
				if (value instanceof Json) {
					Json rj = (Json) value;
					rj.go(out);
				}
				else if (value instanceof Primitive) {
					out.append(value.toString());
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
	
	
	private class Primitive {
		private Object v;
		
		private Primitive(Object o) {
			v = o;
		}
		
		public String toString() {
			return String.valueOf(v);
		}
	}
}
