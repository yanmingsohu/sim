// CatfoOD 2012-2-29 下午06:07:33 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.cmd;

import java.util.Iterator;

import jym.sim.parser.IComponent;

/**
 * 参数: 1个表达式, 表达式中的字符序列认为是变量, 从全局变量中取出
 * 功能: 如果表达式结果为true, 则执行命令内容, 否则忽略内容
 */
public class C_if extends AbsCommand {

	public Iterator<IComponent> getItem() {
		throw new UnsupportedOperationException();
	}

}
