// CatfoOD 2010-10-16 обнГ09:14:18

package jym.sim.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class FromMap extends Json {
	
	private Map<?,?> map;
	
	
	public FromMap(Map<?,?> map) {
		this.map = map;
	}

	@Override
	public void go(Appendable out) throws IOException {
		Iterator<?> itrKey = map.keySet().iterator();
		while (itrKey.hasNext()) {
			Object key = itrKey.next();
			Object val = map.get(key);
			
			super.setOrPrimitive(key, val);
		}
		super.go(out);
	}

}
