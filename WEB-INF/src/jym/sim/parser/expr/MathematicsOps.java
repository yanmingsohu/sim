// CatfoOD 2012-3-1 上午09:49:58 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.expr;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class MathematicsOps {

	/** 除法运算保留小数 */
	public final static int DIV_SCALE = 9;
	
	
	/**  +  */
	public final static class ADD extends Opt {
		public BigDecimal get() {
			return left().get().add(right().get());
		}
		public int level() {
			return 30;
		}
	}
	
	/**  -  */
	public final static class SUB extends Opt { 
		public BigDecimal get() {
			return left().get().subtract(right().get());
		}
		public int level() {
			return 30;
		}
	}
	
	/**  *  */
	public final static class MUL extends Opt { 
		public BigDecimal get() {
			return left().get().multiply(right().get());
		}
		public int level() {
			return 35;
		}
	}
	
	/**  /  */
	public final static class DIV extends Opt { 
		public BigDecimal get() {
			return left().get().divide(right().get(), DIV_SCALE, RoundingMode.HALF_UP);
		}
		public int level() {
			return 35;
		}
	}
	
}
