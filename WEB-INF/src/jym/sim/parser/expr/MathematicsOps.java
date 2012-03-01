// CatfoOD 2012-3-1 上午09:49:58 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.expr;


public class MathematicsOps {

	/**  +  */
	public final static Opt ADD = new Opt() {
		public void compute() {
			set( left().get().add(right().get()) );
		}
	};
	
	/**  -  */
	public final static Opt SUB = new Opt() {
		public void compute() {
			set( left().get().subtract(right().get()) );
		}
	};
	
	/**  *  */
	public final static Opt MUL = new Opt() {
		public void compute() {
			set( left().get().multiply(right().get()) );
		}
	};
	
	/**  /  */
	public final static Opt DIV = new Opt() {
		public void compute() {
			set( left().get().divide(right().get()) );
		}
	};
	
}
