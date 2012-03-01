// CatfoOD 2012-3-1 上午09:28:44 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.expr;

import java.math.BigDecimal;

/**
 * 常量
 */
public class ConstVal extends AbsVal implements IVal {
	
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
	
}
