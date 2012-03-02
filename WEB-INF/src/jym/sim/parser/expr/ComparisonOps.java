// CatfoOD 2012-3-2 下午01:14:42 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.expr;

import java.math.BigDecimal;


public class ComparisonOps {
	
	public static final BigDecimal TRUE  = BigDecimal.ONE;
	public static final BigDecimal FALSE = BigDecimal.ZERO;
	
	
	/** == */
	public static final class EQ extends Opt {
		public BigDecimal get() {
			return left().get().compareTo(right().get()) == 0 ? TRUE : FALSE;
		}
		public int _level() {
			return 27;
		}
	}
	
	/** != */
	public static final class UEQ extends Opt {
		public BigDecimal get() {
			return left().get().compareTo(right().get()) != 0 ? TRUE : FALSE;
		}
		public int _level() {
			return 27;
		}
	}
	
	/** >= */
	public static final class GEQ extends Opt {
		public BigDecimal get() {
			return left().get().compareTo(right().get()) >= 0 ? TRUE : FALSE;
		}
		public int _level() {
			return 28;
		}
	}
	
	/** <= */
	public static final class LEQ extends Opt {
		public BigDecimal get() {
			return left().get().compareTo(right().get()) <= 0 ? TRUE : FALSE;
		}
		public int _level() {
			return 28;
		}
	}
	
	/** > */
	public static final class GE extends Opt {
		public BigDecimal get() {
			return left().get().compareTo(right().get()) > 0 ? TRUE : FALSE;
		}
		public int _level() {
			return 28;
		}
	}
	
	/** < */
	public static final class LE extends Opt {
		public BigDecimal get() {
			return left().get().compareTo(right().get()) < 0 ? TRUE : FALSE;
		}
		public int _level() {
			return 28;
		}
	}
}
