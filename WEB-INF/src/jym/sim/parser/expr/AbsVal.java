// CatfoOD 2012-3-1 下午04:14:52 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.expr;

import java.math.BigDecimal;


public abstract class AbsVal implements IVal {


	public final void set(String v) {
		try {
			set( new BigDecimal(v) );
		} catch(Exception e) {
			set( BigDecimal.ZERO );
		}
	}
	
	public final void set(Object o) {
		if (o == null) {
			set(BigDecimal.ZERO);
		} else {
			set(o.toString());
		}
	}
	
	public final void set(boolean b) {
		set( b ? BigDecimal.ONE : BigDecimal.ZERO );
	}
	
}
