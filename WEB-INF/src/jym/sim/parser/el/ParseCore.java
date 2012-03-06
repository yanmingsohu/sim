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
import java.util.NoSuchElementException;
import java.util.zip.CRC32;

import jym.sim.parser.IComponent;
import jym.sim.parser.IItem;
import jym.sim.parser.IItemFactory;
import jym.sim.parser.ILineCounter;
import jym.sim.parser.ObjectAttribute;
import jym.sim.parser.Type;
import jym.sim.parser.cmd.CommandFactory;
import jym.sim.parser.cmd.ICommand;

/**
 * 该类, 线程不安全
 */
public class ParseCore {
	
	private Map<String, IItem> variables;
	private List<IComponent> m_items;
	private IItemFactory factory;
	private CommandFactory cfact;
	private ILineCounter lc;
	private Reader reader;
	private CRC32 crc;

	/**
	 * reader 可能已经读取过, 当函数返回, reader 不会被关闭
	 * 为扩展'命令'功能做准备
	 */
	public ParseCore(Reader reader, IItemFactory fact, ILineCounter lc) throws IOException {
		factory 	= fact; 
		variables	= new HashMap<String, IItem>();
		m_items		= new ArrayList<IComponent>();
		cfact		= CommandFactory.instance;
		this.lc		= lc;
		this.reader	= reader;
		crc			= new CRC32();
		
		process(m_items);
	}
	
	private void process(List<IComponent> items) throws IOException {
		StringBuilder buff = new StringBuilder();
		boolean isVar = false;
		boolean isCmd = false;
		int ch = 0;
		
		while (true) {
			ch = reader.read();
			crc.update(ch);

			if (ch=='\r') continue;
			
			if (ch=='\n') {
				if (isCmd) {
					/* 碰到换行则一行命令读取完成 */
					addCmd(buff, items);
					isCmd = false;
					continue;
				}
				
				lc.add();
				addText(buff, items);
				items.add(factory.create(Type.ENT));
				continue;
			}
			
			if (isCmd) {
				/* 什么都不做, 用于跳过下面的判断, 
				 * 并把字符直接加入buff中 */
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
								// 吃掉之后的换行符 
								/*
								int cc;
								do {
									reader.mark(1);
									cc = reader.read();
								} while (cc == '\n' || cc == '\r');
								reader.reset();
								*/
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

			if (ch >= 0) {
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
		ICommand cmd = cfact.create(str.toString());
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
	 * <b>创建变量的字符串参数一定不含有空格</b>
	 * @see jym.sim.parser.IItem#init 变量的init()方法初始化约定
	 */
	private void addVar(StringBuilder str, List<IComponent> items) throws IOException {
		String varname = str.toString().trim();
		
		if (varname.length() > 0) {
			try {
				ObjectAttribute.checkVarName(varname);
			} catch(IllegalArgumentException e) {
				throw new IOException(e.getMessage());
			}
			
			IItem item = factory.create(Type.VAR);
			item.init(varname);
			
			String rootname;
			int ri = varname.indexOf('.');
			if (ri > 0) {
				rootname = varname.substring(0, ri);
			} else {
				rootname = varname;
			}
			
			IItem old = variables.get(rootname);
			if (old != null) {
				item.init(null, null, old);
			} else {
				variables.put(rootname, item);
			}
			items.add(item);
			str.setLength(0);
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
	 * 返回文件校验值, 该校验值在文件读取之后就不再改变
	 */
	public long getCrc() {
		return crc.getValue();
	}

	/**
	 * 遇到ICommand元素会解开该迭代器
	 */
	private class ItemIterator implements Iterator<IItem> {

		private LinkedList<Iterator<IComponent>> stack;
		private Iterator<IComponent> itr;
		private IItem next;
		private boolean check;
		
		ItemIterator() {
			stack	= new LinkedList<Iterator<IComponent>>();
			itr		= m_items.iterator();
			check	= true;
		}
		
		private void _next() {
			if (!check) return;
			check = false;
			
			while (itr != null) {
				if (itr.hasNext()) {
					IComponent comp = itr.next();
					
					if (comp instanceof ICommand) {
						stack.addFirst(itr); // push()
						ICommand cmd = (ICommand) comp;
						itr = cmd.getItem();
					}
					else if(comp instanceof IItem) {
						next = (IItem) comp;
						break;
					}
					else {
						throw new IllegalStateException("不支持的类型: " + comp.getClass());
					}
				} else {
					itr = stack.poll(); // pop()
					if (itr == null) {
						itr = null;
						next = null;
						break;
					}
				}
			}
		}
		
		public boolean hasNext() {
			_next();
			return next != null;
		}

		public IItem next() {
			_next();
			if (next == null) throw new NoSuchElementException();
			check = true;
			return next;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
