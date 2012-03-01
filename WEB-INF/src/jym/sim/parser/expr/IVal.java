// CatfoOD 2012-3-1 上午10:04:51 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.expr;

import java.math.BigDecimal;


/**
 * 可以返回值的对象, 用于计算<br>
 * 值总是数字类型,<br> 
 * 布尔值非0为true, 0为false<br>
 * 对象的toString()返回非0值, 则非null为true, 否则false<br>
 */
public interface IVal {

	/** 取得变量, 不会为null, 或者返回计算的结果 */
	BigDecimal get();
	
	/** 设置变量, 如果为null, 则设置为0 */
	void set(BigDecimal v);
	
}
