// CatfoOD 2010-10-16 обнГ07:57:16

package jym.sim.json;

import java.io.IOException;

public class Primitive implements IGo {

	private Object v;
	
	public Primitive(Object o) {
		v = o;
	}

	public void go(Appendable json) throws IOException {
		json.append(String.valueOf(v));
	}
}
