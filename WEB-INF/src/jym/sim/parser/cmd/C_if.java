// CatfoOD 2012-2-29 下午06:07:33 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.cmd;

import java.math.BigDecimal;
import java.util.Iterator;

import jym.sim.parser.IComponent;
import jym.sim.parser.expr.ExprException;
import jym.sim.parser.expr.Expression;

/**
 * 参数: 1个表达式, 表达式中的字符序列认为是变量, 从全局变量中取出<br>
 * 功能: 如果表达式结果为true, 则执行命令内容, 否则忽略内容
 */
public class C_if extends AbsCommand {

	public Iterator<IComponent> getItem() {
		try {
			Expression exp = compile(0);
			BigDecimal ret = exp.compute();
			if ( ret.compareTo(BigDecimal.ZERO) != 0 ) {
				return content.iterator();
			}
		} catch (ExprException e) {
			e.printStackTrace();
		}
		return NULL_ITR;
	}

}
