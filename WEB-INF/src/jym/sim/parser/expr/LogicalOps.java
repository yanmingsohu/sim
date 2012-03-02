// CatfoOD 2012-3-2 下午01:24:55 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.expr;

import java.math.BigDecimal;


public class LogicalOps extends ComparisonOps {

	/**  &&  */
	public static final class AND extends Opt {
		public BigDecimal get() {
			if (left().get().compareTo(BigDecimal.ZERO) != 0) {
				return right().get();
			}
			return FALSE;
		}
		public int level() {
			return 25;
		}
	}
	
	/**  ||  */
	public static final class OR extends Opt {
		public BigDecimal get() {
			BigDecimal l = left().get();
			if (l.compareTo(BigDecimal.ZERO) != 0) {
				return l;
			}
			return right().get();
		}
		public int level() {
			return 25;
		}
	}
	
}
