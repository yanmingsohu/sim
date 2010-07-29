// CatfoOD 2010-7-28 ÏÂÎç02:10:10 yanming-sohu@sohu.com/@qq.com

package jym.sim.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Json implements IjSon {

	private Map<String,Object> map;
	
	
	public Json() {
		map = new HashMap<String,Object>();
	}
	
	
	public void set(Object name, Object value) {
		map.put(formatJson(name.toString() ), value);
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
	
	public void go(Appendable json) throws IOException {
		Iterator<String> it = map.keySet().iterator();
		
		json.append('{');
			while (it.hasNext()) {
				String name = it.next();
				Object value = map.get(name);
				
				json.append('"').append(name).append('"');
				json.append(':');
				if (value instanceof Json) {
					Json rj = (Json) value;
					rj.go(json);
				}
				else {
					json.append('"').append(
							formatJson( String.valueOf(value) )
						).append('"');
				}
				
				if (it.hasNext()) {
					json.append(',');
				}
			}
		json.append('}');
	}

	protected static String formatJson(String str) {
		return	JSonFormater.frm(str);
	}
	
}
