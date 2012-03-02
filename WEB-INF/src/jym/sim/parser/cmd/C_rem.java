// CatfoOD 2012-2-29 下午06:45:26 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.cmd;

import java.util.Iterator;

import jym.sim.parser.IComponent;

/**
 * 参数: 无<br>
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

}
