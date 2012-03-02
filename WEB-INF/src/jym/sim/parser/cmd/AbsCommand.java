// CatfoOD 2012-2-29 下午05:47:09 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.cmd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jym.sim.parser.IComponent;
import jym.sim.parser.IItem;
import jym.sim.parser.expr.ExprException;
import jym.sim.parser.expr.Expression;

/**
 * 框架实现
 */
public abstract class AbsCommand implements ICommand {
	
	public final char NOT_SPLIT_PARAMS = 0;
	
	/** 保存内容对象的列表, 不会为null */
	protected final List<IComponent> content;
	/** 全局变量列表, 不会为null */
	protected Map<String, IItem> vars;
	/** 传递给命令的参数初始为0长度数组, 不会为null */
	protected final List<String> params;
	/** 空迭代器, 用于忽略内容体 */
	protected final NulIterator NULL_ITR = new NulIterator();
	
	
	public AbsCommand() {
		content = new ArrayList<IComponent>();
		params = new ArrayList<String>();
	}

	public List<IComponent> getBag() {
		return content;
	}

	public void setParameter(final String p) {
		if (p != null) {
			final char sp_ch = splitCh(p);
			if (sp_ch == 0) {
				params.add(p);
				return;
			}
			
			StringBuilder buff = new StringBuilder();
			int  i  = -1;
			char ch =  0;
			
			while (++i < p.length()) {
				ch = p.charAt(i);
				if (ch == sp_ch) {
					addParm(buff);
					buff.setLength(0);
					continue;
				}
				buff.append(ch);
			}
			addParm(buff);
		}
	}
	
	private void addParm(StringBuilder buff) {
		String p = buff.toString().trim();
		if (p.length() > 0) {
			params.add(p);
		}
	}

	public void setVariableBag(Map<String, IItem> vars) {
		this.vars = vars;
	}
	
	/**
	 * 返回用来分隔参数字符串的字符, 如果返回数字0
	 * 则把整个参数作为params的第一个元素
	 * <br>
	 * 子类只要重写该方法即可分隔参数列表, 默认返回0
	 * 
	 * @param waitSplit - 等待被分解的字符串
	 */
	protected char splitCh(String waitSplit) {
		return NOT_SPLIT_PARAMS;
	}
	
	/**
	 * 执行表达式并返回结果, 表达式中的字符序列认为是变量, 
	 * 并从全局变量列表中取值
	 * @throws ExprException 
	 */
	protected Expression compile(String exp_s) throws ExprException {
		return new Expression(exp_s, vars);
	}
	
	/**
	 * 从解析后的参数列表中取字符串参数并编译为表达式
	 * @throws ExprException 
	 */
	protected Expression compile(int param_index) throws ExprException {
		return compile(params.get(param_index));
	}
	
	protected void error(String msg) throws ExprException {
		throw new ExprException(this.getClass() + ": " + msg);
	}
	
	/**
	 * 空的迭代器
	 */
	protected static class NulIterator implements Iterator<IComponent> {
		public boolean hasNext() { 	return false;	}
		public IComponent next() {	return null;	}
		public void remove() { }
	}
}
