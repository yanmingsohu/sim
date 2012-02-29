// CatfoOD 2012-2-28 下午12:32:48 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.el;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jym.sim.parser.IComponent;
import jym.sim.parser.IItem;
import jym.sim.parser.IItemFactory;
import jym.sim.parser.ILineCounter;
import jym.sim.parser.Type;
import jym.sim.parser.cmd.CommandFactory;
import jym.sim.parser.cmd.ICommand;

/**
 * 该类, 线程不安全
 */
public class ParseCore {
	
	private Map<String, IItem> variables;
	private IItemFactory factory;
	private List<IComponent> m_items;
	private ILineCounter lc;
	private Reader reader;

	/**
	 * reader 可能已经读取过, 当函数返回, reader 不会被关闭
	 * 为扩展'命令'功能做准备
	 */
	public ParseCore(Reader reader, IItemFactory fact, ILineCounter lc) throws IOException {
		factory 	= fact; 
		variables	= new HashMap<String, IItem>();
		m_items		= new ArrayList<IComponent>();
		this.lc		= lc;
		this.reader	= reader;
		
		process(m_items);
	}
	
	private void process(List<IComponent> items) throws IOException {
		StringBuilder buff = new StringBuilder();
		boolean isVar = false;
		boolean isCmd = false;
		int ch = 0;
		
		while (true) {
			ch = reader.read();

			if (ch=='\r') continue;
			
			if (ch=='\n') {
				lc.add();
				
				if (isCmd) {
					/* 碰到换行则一行命令读取完成 */
					addCmd(buff, items);
					isCmd = false;
					continue;
				}
				
				addText(buff, items);
				items.add(factory.create(Type.ENT));
				continue;
			}
			
			if (isCmd) {
				/* 什么都不做, 用于跳过下面的判断, 
				 * 并把字符直接加入buff中 */
			}
			else if (ch=='"') {
				throw new IOException("不能有双引号");
			}
			else if (isVar) {
				if (ch=='}') {
					addVar(buff, items);
					isVar = false;
					continue;
				}
			}
			/* 动态变量使用语法: ${...} */
			else if (ch=='$') {
				reader.mark(3);
				ch = (char) reader.read();
				
				if (ch!='$') {
					if (ch=='{') {
						addText(buff, items);
						isVar = true;
						continue;
					} else {
						reader.reset();
					}
				}
			}
			/* 命令语法 #xxx, 要输出'#'字符使用'##' */
			else if (ch == '#') {
				reader.mark(3);
				ch = (char) reader.read();
				
				if (ch != '#') {
					addText(buff, items);
					
					if (ch == 'e') {
						ch = (char) reader.read();
						if (ch == 'n') {
							ch = (char) reader.read();
							if (ch == 'd') {
								// 命令结束
								return;
							}
						}
					}
					reader.reset();
					isCmd = true;
					continue;
				}
			}

			if (ch>=0) {
				buff.append((char)ch);
			} else {
				break;
			}
		}
		
		if (isVar) throw new IOException("引用变量未正确结束");
		if (isCmd) throw new IOException("命令未正确结束");
		
		addText(buff, items);
	}
	
	/**
	 * 创建命令, 并把接下来的内容作为命令控制的内容
	 * 文件中碰到'#end'该方法会返回
	 */
	private void addCmd(StringBuilder str, List<IComponent> items) throws IOException {
		ICommand cmd = CommandFactory.create(str.toString());
		cmd.setVariableBag(variables);
		
		items.add(cmd);
		str.setLength(0);

		process(cmd.getBag());
	}
	
	private void addText(StringBuilder str, List<IComponent> items) throws IOException {
		if (str.length() > 0) {
			IItem item = factory.create(Type.STR);
			item.init(str.toString());
			items.add(item);
			str.setLength(0);
		}
	}
	
	/**
	 * 变量元素约定: 第一个参数为变量名, 第二个为变量值, 第三个为变量引用(引用另一个item中的变量值)
	 * <b>创建变量的字符串参数一定不含有空格</b>
	 */
	private void addVar(StringBuilder str, List<IComponent> items) throws IOException {
		String varname = str.toString().trim();
		
		if (varname.length() > 0) {
			checkVarName(varname);
			
			IItem item = factory.create(Type.VAR);
			item.init(varname);
			
			IItem old = variables.get(item.getText());
			if (old != null) {
				item.init(null, null, old);
			} else {
				variables.put(item.getText(), item);
			}
			items.add(item);
			str.setLength(0);
		}
	}
	
	/**
	 * 检查变量名是否符合java命名规范如:'x.y.z'
	 * @param name
	 * @throws IOException
	 */
	public static void checkVarName(String name) throws IOException {
		char[] ch = name.toCharArray();
		int i = -1;
		int word_len = 0;
		int method = 0;
		
		while (++i < ch.length) {
			if (ch[i] == ' ' || ch[i] == '\t') continue;
			
			if (word_len == 0) {
				if ( !Character.isJavaIdentifierStart(ch[i]) ) {
					throw new IOException("无效的变量名首字母:[" + name + "]");
				}
				
				word_len = 1;
				continue;
			}
			
			if (ch[i] == '(') {
				if (method > 0)
					throw new IOException("不能使用连续的左括号:[" + name + "]");
			
				method = 1;
				continue;
			}

			if (ch[i] == ')') {
				if (method > 1)
					throw new IOException("不能使用连续的右括号:[" + name + "]");
				
				method = 2;
				continue;
			}
			
			if (method == 1) {
				throw new IOException("缺失右括号:[" + name + "]");
			}
			
			if (ch[i] == '.') {
				word_len = 0;
				method = 0;
				continue;
			}

			if (method > 0) {
				throw new IOException("函数调用语法错误:[" + name + "]");
			}
			
			if ( !Character.isJavaIdentifierPart(ch[i]) ) {
				throw new IOException("含有无效的字符:[" + name + "]");
			}
		}	
	}

	public Map<String, IItem> getVariables() {
		return variables;
	}
	
	/**
	 * 如果文件中有该变量返回true
	 */
	public boolean hasVariable(String vname) {
		return variables.containsKey(vname);
	}
	
	/**
	 * 返回解析后的sql文件中元素的迭代器<br>
	 * 如果文件中有变量, 可以影响返回的结果集
	 */
	public Iterator<IItem> getItems() {
		return new ItemIterator();
	}

	/**
	 * 遇到ICommand元素会解开该迭代器
	 */
	private class ItemIterator implements Iterator<IItem> {
		/**XXX 未完成 */
		private LinkedList<Iterator<IComponent>> stack;
		private Iterator<IComponent> itr;
		
		ItemIterator() {
			stack = new LinkedList<Iterator<IComponent>>();
			itr = m_items.iterator();
		}
		
		public boolean hasNext() {
			boolean has = itr.hasNext();
			while (!has) {
				if (!stack.isEmpty()) {
					itr = stack.removeFirst(); // pop()
				} else {
					break;
				}
			}
			return has;
		}

		public IItem next() {
			/**XXX 有问题!!! */
			IComponent comp = itr.next();
			if (comp instanceof ICommand) {
				stack.addFirst(itr);
				ICommand cmd = (ICommand) comp;
				itr = cmd;
			}
			return null;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
