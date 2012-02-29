// CatfoOD 2012-2-29 下午05:47:09 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jym.sim.parser.IComponent;
import jym.sim.parser.IItem;

/**
 * 框架实现
 */
public abstract class AbsCommand implements ICommand {
	
	/** 保存内容对象的列表, 不会为null */
	protected final List<IComponent> content;
	/** 全局变量列表, 不会为null */
	protected Map<String, IItem> vars;
	/** 传递给命令的参数初始为0长度数组, 不会为null */
	protected List<String> params;
	
	
	public AbsCommand() {
		content = new ArrayList<IComponent>();
		params = new ArrayList<String>();
	}

	public List<IComponent> getBag() {
		return content;
	}

	public void setParameter(List<String> p) {
		if (p != null) {
			params = p;
		}
	}

	public void setVariableBag(Map<String, IItem> vars) {
		this.vars = vars;
	}
	
	/**
	 * 执行表达式并返回结果, 表达式中的字符序列认为是变量, 
	 * 并从全局变量列表中取值
	 */
	protected Object eval(String exp) {
		throw new UnsupportedOperationException();
	}
}
