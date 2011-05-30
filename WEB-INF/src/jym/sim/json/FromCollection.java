// CatfoOD 2010-10-16 下午09:05:29

package jym.sim.json;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class FromCollection extends Json {
	
	private Collection<?> col;
	
	
	public FromCollection(Collection<?> c) {
		col = c;
	}
	
	public void go(Appendable out) throws IOException {
		Iterator<?> itr = col.iterator();
		int i = 0;
		while (itr.hasNext()) {
			super.setOrPrimitive(i++, itr.next());
		}
		super.go(out);
	}
}
