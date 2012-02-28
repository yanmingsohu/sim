// CatfoOD 2012-2-28 下午12:32:48 yanming-sohu@sohu.com/@qq.com

package jym.sim.parser.el;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import jym.sim.parser.IItem;
import jym.sim.parser.IItemFactory;
import jym.sim.parser.ILineCounter;
import jym.sim.parser.Type;


public class ParseCore {
	
	private static final Pattern exp = Pattern.compile("^[_A-Za-z][\\.\\$_A-Za-z0-9]*");
	
	private IItemFactory factory;
	private Map<String, IItem> variables;
	private List<IItem> items;

	/**
	 * reader 可能已经读取过, 当函数返回, reader 不会被关闭
	 * 为扩展'命令'功能做准备
	 */
	public ParseCore(Reader reader, IItemFactory fact, ILineCounter lc) throws IOException {
		factory 	= fact; 
		variables	= new HashMap<String, IItem>();
		items		= new ArrayList<IItem>();
		
		StringBuilder buff = new StringBuilder();
		int ch = ' ';
		boolean isVar = false;
		
		while (true) {
			ch = reader.read();

			if (ch=='\r') continue;
			
			if (ch=='\n') {
				lc.add();
				addText(buff);
				items.add(factory.create(Type.ENT));
				continue;
			}
			
			if (ch=='"') {
				throw new IOException("不能有双引号");
			}
			
			if (isVar) {
				if (ch=='}') {
					addVar(buff);
					isVar = false;
					continue;
				}
			}
			/* 动态变量使用语法: ${...} */
			else if (ch=='$') {
				reader.mark(3);
				ch = (char) reader.read();
				
				if (ch=='$') continue;
				
				if (ch=='{') {
					addText(buff);
					isVar = true;
					continue;
				} else {
					reader.reset();
				}
			}

			if (ch>=0) {
				buff.append((char)ch);
			} else {
				break;
			}
		}
		addText(buff);
	}
	
	private void addText(StringBuilder str) throws IOException {
		if (str.length() > 0) {
			IItem item = factory.create(Type.STR);
			item.init(str.toString());
			items.add(item);
			str.setLength(0);
		}
	}
	
	/**
	 * 变量元素约定: 第一个参数为变量名, 第二个为变量值, 第三个为变量引用(引用另一个item中的变量值)
	 */
	private void addVar(StringBuilder str) throws IOException {
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
	
	private void checkVarName(String name) throws IOException {
		if (!exp.matcher(name).matches()) {
			throw new IOException("无效的变量名:[" + name + "]");
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
		return items.iterator();
	}

}
