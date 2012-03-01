// CatfoOD 2012-3-1 上午09:26:28 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.expr;

/**
 * 一个表达式, 左值和右值
 * @see Val
 */
public abstract class Opt extends ConstVal {

	private IVal left;
	private IVal right;
	
	
	public void set(IVal l, IVal r) {
		left = l;
		right = r;
	}
	
	public IVal left() {
		return left;
	}
	
	public IVal right() {
		return right;
	}
	
	/**
	 * <code>val = left op right</code><br/>
	 */
	public abstract void compute();
	
}
