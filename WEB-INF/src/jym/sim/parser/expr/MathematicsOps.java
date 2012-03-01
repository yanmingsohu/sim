// CatfoOD 2012-3-1 上午09:49:58 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.expr;

import java.math.BigDecimal;


public class MathematicsOps {

	/**  +  */
	public final static Opt ADD() {
		return new Opt() {
			public BigDecimal get() {
				return left().get().add(right().get());
			}
			public int level() {
				return 30;
			}
		};
	}
	
	/**  -  */
	public final static Opt SUB() { 
		return new Opt() {
			public BigDecimal get() {
				return left().get().subtract(right().get());
			}
			public int level() {
				return 30;
			}
		};
	}
	
	/**  *  */
	public final static Opt MUL() { 
		return new Opt() {
			public BigDecimal get() {
				return left().get().multiply(right().get());
			}
			public int level() {
				return 35;
			}
		};
	}
	
	/**  /  */
	public final static Opt DIV() { 
		return new Opt() {
			public BigDecimal get() {
				return left().get().divide(right().get());
			}
			public int level() {
				return 35;
			}
		};
	}
	
}
