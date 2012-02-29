// CatfoOD 2012-2-29 下午06:45:26 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.cmd;

import java.util.Iterator;
import java.util.List;

import jym.sim.parser.IComponent;
import jym.sim.util.Tools;

/**
 * 参数: 无
 * 功能: 注释命令, 内容体都会被忽略
 */
public class C_rem extends AbsCommand {

	public Iterator<IComponent> getItem() {
		return new Iterator<IComponent>() {

			public boolean hasNext() {
				return false;
			}

			public IComponent next() {
				return null;
			}

			public void remove() {
			}
		};
	}

	@Override
	public void setParameter(List<String> p) {
		super.setParameter(p);
		Tools.pl("params: ", p);
	}

}
