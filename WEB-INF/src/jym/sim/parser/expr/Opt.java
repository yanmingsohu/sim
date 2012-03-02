// CatfoOD 2012-3-1 上午09:26:28 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.expr;

import java.math.BigDecimal;

/**
 * 一个表达式, 左值和右值
 * @see Val
 */
public abstract class Opt extends AbsVal implements IVal {

	private IVal left;
	private IVal right;
	
	
	public void left(IVal l) {
		left = l;
	}
	
	public void right(IVal r) {
		right = r;
	}
	
	public IVal left() {
		return left;
	}
	
	public IVal right() {
		return right;
	}
	
	/**
	 * 默认不支持该方法
	 */
	public void set(BigDecimal v) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * <code>return [left op right]</code><br/>
	 */
	public abstract BigDecimal get();
	
	/**
	 * 运算符优先级, 值越大优先级越高
	 */
	protected abstract int level();
	
	
}
