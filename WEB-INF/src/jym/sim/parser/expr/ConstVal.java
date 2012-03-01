// CatfoOD 2012-3-1 上午09:28:44 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.expr;

import java.math.BigDecimal;

/**
 * 常量
 */
public class ConstVal implements IVal {
	
	private BigDecimal val;
	
	
	public ConstVal() {
		val = BigDecimal.ZERO;
	}
	
	public ConstVal(String v) {
		set(v);
	}
	
	public BigDecimal get() {
		return val;
	}
	
	public void set(BigDecimal v) {
		if (v == null) {
			val = BigDecimal.ZERO;
		} else {
			val = v;
		}
	}
	
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
